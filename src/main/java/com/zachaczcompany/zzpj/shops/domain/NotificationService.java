package com.zachaczcompany.zzpj.shops.domain;

import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.commons.response.Response;
import com.zachaczcompany.zzpj.commons.response.Success;
import io.vavr.Tuple;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.util.List;

@Service
public class NotificationService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final NotificationListRepository repository;
    private final ShopRepository shopRepository;
    private MimeMessageHelper helper;

    @Autowired
    public NotificationService(TemplateEngine templateEngine, JavaMailSender javaMailSender,
                               NotificationListRepository repository, ShopRepository shopRepository) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.repository = repository;
        this.shopRepository = shopRepository;
    }

    public void sendNotificationsToShopList(Shop shop) {
        List<NotificationList> notificationList = repository.findByShopId(shop.getId());

        String title = shop.getName() + " is getting full!";
        String description = shop.getName() + " is currently at >75% of its capacity. There is a possibility " +
                "that you'll have to wait in a queue!";

        Context context = new Context();
        context.setVariable("title", title);
        context.setVariable("description", description);
        String body = templateEngine.process("emailNotificationTemplate", context);

        notificationList.forEach(element -> {
            sendEmail(element.getEmail(), title, body);
            repository.delete(element);
        });
    }

    public void deleteExpiredNotifications() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<NotificationList> expired = repository.findByNotificationEndGreaterThan(currentTime);
        expired.forEach(repository::delete);
    }

    public Response addUserToNotificationList(String email, long shopId, Timestamp endTimestamp) {
        NotificationList notificationList = new NotificationList(shopId, email, endTimestamp);
        return doesntExist(notificationList).combine(timestampBeforeCurrentTime(notificationList))
                                            .combine(shopDoesntExist(notificationList))
                                            .ap(Tuple::of)
                                            .fold(Error::concatCodes, tuple -> Success
                                                    .ok(repository.save(notificationList)));
    }

    Validation<Error, NotificationList> doesntExist(NotificationList notificationList) {
        return !repository.existsByEmail(notificationList.getEmail()) ? Validation.valid(notificationList) :
                Validation.invalid(Error.badRequest("NOTIFICATION_FOR_THIS_USER_EXISTS"));
    }

    Validation<Error, NotificationList> timestampBeforeCurrentTime(NotificationList notificationList) {
        return notificationList.getNotificationEnd().after(new Timestamp(System.currentTimeMillis()))
                ? Validation.valid(notificationList) : Validation.invalid(Error.badRequest("TIMESTAMP_BEFORE_CURRENT_TIME"));
    }

    Validation<Error, NotificationList> shopDoesntExist(NotificationList notificationList) {
        return shopRepository.existsById(notificationList.getShopId())
                ? Validation.valid(notificationList) : Validation.invalid(Error.badRequest("SHOP_DOES_NOT_EXIST"));
    }

    private void sendEmail(String to, String title, String content) {
        MimeMessage mail = javaMailSender.createMimeMessage();

        Try.of(() -> {
            helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom("shopfinderappemailsender@gmail.com");
            helper.setSubject(title);
            helper.setText(content, true);
            return helper;
        });
        javaMailSender.send(mail);
    }
}

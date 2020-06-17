package com.zachaczcompany.zzpj.shops.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface NotificationListRepository extends CrudRepository<NotificationList, Long> {
    List<NotificationList> findByShopId(long shopId);
    List<NotificationList> findByNotificationEndGreaterThan(Timestamp currentTime);
    boolean existsByEmail(String email);
}

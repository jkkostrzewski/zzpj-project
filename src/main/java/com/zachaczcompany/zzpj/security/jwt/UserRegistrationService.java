package com.zachaczcompany.zzpj.security.jwt;

import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.commons.response.Response;
import com.zachaczcompany.zzpj.commons.response.Success;
import com.zachaczcompany.zzpj.security.OwnerSignUpDto;
import com.zachaczcompany.zzpj.security.annotations.IsOwner;
import com.zachaczcompany.zzpj.security.auth.database.UserEntity;
import com.zachaczcompany.zzpj.security.auth.database.UserRepository;
import com.zachaczcompany.zzpj.security.configuration.UserRole;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import com.zachaczcompany.zzpj.shops.domain.Shop;
import com.zachaczcompany.zzpj.shops.domain.ShopFacade;
import io.vavr.control.Validation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShopFacade shopFacade;

    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder, ShopFacade shopFacade) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.shopFacade = shopFacade;
    }


    public Response registerOwner(OwnerSignUpDto ownerSignUpDto) {
        Validation<Error, ShopCreateDto> validShop = Validation.valid(ownerSignUpDto.getShop());
        return canRegister(ownerSignUpDto.getCredentials())
                .combine(validShop)
                .ap(this::registerAndCreateShop)
                .fold(Error::concatCodes, Function.identity());
    }

    private Success<UserEntity> registerAndCreateShop(UserSignUp userSignUp, ShopCreateDto shopCreateDto) {
        var shop = shopFacade.createShop(shopCreateDto);
        var user = registerUserAccount(userSignUp, UserRole.SHOP_OWNER, shop);
        return Success.accepted(user);
    }

    @IsOwner
    public Response registerEmployee(UserSignUp userSignUp) {
        var shop = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        System.out.println(shop);
        return canRegister(userSignUp)
                .fold(Function.identity(), UserRegistrationService::registerAndAssignToShop);
    }

    private static Response registerAndAssignToShop(UserSignUp userSignUp) {
        return null;
    }

    private UserEntity registerUserAccount(UserSignUp userSignUp, UserRole userRole, Shop shop) {
        String encodePassword = passwordEncoder.encode(userSignUp.getPassword());
        UserEntity userEntity = new UserEntity(userSignUp.getUsername(), encodePassword, userRole, shop);
        return userRepository.save(userEntity);
    }

    private Validation<Error, UserSignUp> canRegister(UserSignUp userSignUp) {
        return userRepository.existsByUsername(userSignUp.getUsername()) ? Validation.valid(userSignUp) : Validation.invalid(Error.badRequest("USERNAME_ALREADY_IN_USE"));
    }
}

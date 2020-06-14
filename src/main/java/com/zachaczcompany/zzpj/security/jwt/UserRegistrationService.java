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
import io.vavr.control.Either;
import io.vavr.control.Validation;
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

    private static Response registerAndAssignToShop(UserSignUp userSignUp) {
        throw new IllegalStateException("NOT IMPLEMENTED YET");
    }

    public Response registerOwner(OwnerSignUpDto ownerSignUpDto) {
        final var shopData = ownerSignUpDto.getShop();
        return canRegister(ownerSignUpDto.getCredentials())
                .toEither()
                .flatMap(credentials -> registerAndCreateShop(credentials, shopData))
                .fold(Function.identity(), Function.identity());
    }

    private Either<Error, Success<UserEntity>> registerAndCreateShop(UserSignUp userSignUp, ShopCreateDto shopCreateDto) {
        return shopFacade.createShop(shopCreateDto)
                         .map(s -> registerUserAccount(userSignUp, UserRole.SHOP_OWNER, s))
                         .map(Success::accepted);
    }

    @IsOwner
    public Response registerEmployee(UserSignUp userSignUp) {
        return canRegister(userSignUp)
                .fold(Function.identity(), UserRegistrationService::registerAndAssignToShop);
    }

    private UserEntity registerUserAccount(UserSignUp userSignUp, UserRole userRole, Shop shop) {
        String encodePassword = passwordEncoder.encode(userSignUp.getPassword());
        UserEntity userEntity = new UserEntity(userSignUp.getUsername(), encodePassword, userRole, shop);
        return userRepository.save(userEntity);
    }

    private Validation<Error, UserSignUp> canRegister(UserSignUp userSignUp) {
        return userRepository.existsByUsername(userSignUp.getUsername()) ? Validation.valid(userSignUp) : Validation
                .invalid(Error.badRequest("USERNAME_ALREADY_IN_USE"));
    }
}

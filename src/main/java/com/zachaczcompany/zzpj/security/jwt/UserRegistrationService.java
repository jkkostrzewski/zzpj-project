package com.zachaczcompany.zzpj.security.jwt;

import com.zachaczcompany.zzpj.commons.response.Error;
import com.zachaczcompany.zzpj.commons.response.Response;
import com.zachaczcompany.zzpj.commons.response.Success;
import com.zachaczcompany.zzpj.security.OwnerSignUpDto;
import com.zachaczcompany.zzpj.security.annotations.IsEmployee;
import com.zachaczcompany.zzpj.security.annotations.IsOwner;
import com.zachaczcompany.zzpj.security.auth.database.UserEntity;
import com.zachaczcompany.zzpj.security.auth.database.UserRepository;
import com.zachaczcompany.zzpj.security.configuration.UserRole;
import com.zachaczcompany.zzpj.security.jwt.exceptions.UserNotAllowedToPerform;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import com.zachaczcompany.zzpj.shops.domain.Shop;
import com.zachaczcompany.zzpj.shops.domain.ShopFacade;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
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

    private Either<Error, UserCreatedDTO> registerAndAssignToShop(String ownerName, UserSignUp userSignUp) {
        return Option.ofOptional(userRepository.findByUsername(ownerName)).map(UserEntity::getShop)
                     .map(s -> registerUserAccount(userSignUp, UserRole.SHOP_EMPLOYEE, s))
                     .map(UserCreatedDTO::new).toEither(Error.badRequest("CANNOT_ADD_EMPLOYEE"));
    }

    public Response registerOwner(OwnerSignUpDto ownerSignUpDto) {
        final var shopData = ownerSignUpDto.getShop();
        return canRegister(ownerSignUpDto.getCredentials())
                .toEither()
                .flatMap(credentials -> registerAndCreateShop(credentials, shopData))
                .fold(Function.identity(), Success::accepted);
    }

    private Either<Error, UserCreatedDTO> registerAndCreateShop(UserSignUp userSignUp, ShopCreateDto shopCreateDto) {
        return shopFacade.createShop(shopCreateDto)
                         .map(s -> registerUserAccount(userSignUp, UserRole.SHOP_OWNER, s))
                         .map(UserCreatedDTO::new);
    }

    @IsOwner
    Response registerEmployee(String ownerName, UserSignUp userSignUp) {
        return canRegister(userSignUp)
                .toEither()
                .flatMap(employee -> registerAndAssignToShop(ownerName, employee))
                .fold(Function.identity(), Success::accepted);
    }

    @IsEmployee
    Response changePassword(String username, String password) {
        return Option.ofOptional(userRepository.findByUsername(username))
                     .peek(userEntity -> userEntity.setPassword(password))
                     .map(userRepository::save)
                     .toEither(Error.badRequest("USER_NOT_FOUND")).fold(Function.identity(), Success::ok);
    }

    @IsOwner
    Response deleteEmployee(String ownerName, String username) {
        boolean isEmployeeOwner = userRepository.findByUsername(ownerName).map(UserEntity::getShop)
                                                .equals(userRepository.findByUsername(username)
                                                                      .map(UserEntity::getShop));
        return Try.run(() -> possibleDeleteOfEmployee(isEmployeeOwner, username))
                  .toEither(Error.badRequest("CANNOT_DELETE_USER"))
                  .fold(Function.identity(), Success::ok);
    }

    private void possibleDeleteOfEmployee(boolean employeeOwner, String username) throws UserNotAllowedToPerform {
        if (employeeOwner) {
            Option.ofOptional(userRepository.findByUsername(username))
                  .peek(userRepository::delete);
        } else {
            throw new UserNotAllowedToPerform("Not shop owner employee");
        }
    }

    private UserEntity registerUserAccount(UserSignUp userSignUp, UserRole userRole, Shop shop) {
        String encodePassword = passwordEncoder.encode(userSignUp.getPassword());
        UserEntity userEntity = new UserEntity(userSignUp.getUsername(), encodePassword, userRole, shop);
        return userRepository.save(userEntity);
    }

    private Validation<Error, UserSignUp> canRegister(UserSignUp userSignUp) {
        return !userRepository.existsByUsername(userSignUp.getUsername()) ? Validation.valid(userSignUp) : Validation
                .invalid(Error.badRequest("USERNAME_ALREADY_IN_USE"));
    }
}

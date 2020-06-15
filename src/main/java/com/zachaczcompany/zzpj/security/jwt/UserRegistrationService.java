package com.zachaczcompany.zzpj.security.jwt;

import com.zachaczcompany.zzpj.security.auth.database.UserEntity;
import com.zachaczcompany.zzpj.security.auth.database.UserRepository;
import com.zachaczcompany.zzpj.security.configuration.UserRole;
import com.zachaczcompany.zzpj.security.jwt.exceptions.UserAlreadyExistsException;
import com.zachaczcompany.zzpj.shops.domain.ShopRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.zachaczcompany.zzpj.security.configuration.UserRole.SHOP_EMPLOYEE;
import static com.zachaczcompany.zzpj.security.configuration.UserRole.SHOP_OWNER;

@Service
public class UserRegistrationService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ShopRepository shopRepository;

    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder, ShopRepository shopRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.shopRepository = shopRepository;
    }


    /*FIXME wywalic transctional po dodaniu rejestracji z tworzeniu sklepu
     * teraz jak iterator.next() wywali npe, to uzytkownik nie powinien zostac zarejestrowany
     * czyli nie bedzie ownera bez sklepu. mam nadzieje
     */
    @Transactional
    public void registerOwner(UserSignUp userSignUp) throws UserAlreadyExistsException {
        registerUserAccount(userSignUp, SHOP_OWNER);
    }

    public void registerEmployee(UserSignUp userSignUp) throws UserAlreadyExistsException {
        registerUserAccount(userSignUp, SHOP_EMPLOYEE);
    }

    private void registerUserAccount(UserSignUp userSignUp, UserRole userRole) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(userSignUp.getUsername())) {
            throw new UserAlreadyExistsException("There is an user with that username: " + userSignUp.getUsername());
        }

        String encodePassword = passwordEncoder.encode(userSignUp.getPassword());
        UserEntity userEntity = new UserEntity(userSignUp.getUsername(), encodePassword, userRole, shopRepository
                .findAll().iterator().next());
        userRepository.save(userEntity);
    }
}

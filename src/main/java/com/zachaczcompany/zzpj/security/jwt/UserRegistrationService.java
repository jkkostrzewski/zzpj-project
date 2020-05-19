package com.zachaczcompany.zzpj.security.jwt;

import com.zachaczcompany.zzpj.security.auth.database.UserEntity;
import com.zachaczcompany.zzpj.security.auth.database.UserRepository;
import com.zachaczcompany.zzpj.security.configuration.UserRole;
import com.zachaczcompany.zzpj.security.jwt.exceptions.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.zachaczcompany.zzpj.security.configuration.UserRole.SHOP_EMPLOYEE;
import static com.zachaczcompany.zzpj.security.configuration.UserRole.SHOP_OWNER;

@Service
public class UserRegistrationService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewOwner(UserSignUp userSignUp) throws UserAlreadyExistsException {
        registerNewUserAccount(userSignUp, SHOP_OWNER);
    }

    public void registerNewEmployee(UserSignUp userSignUp) throws UserAlreadyExistsException {
        registerNewUserAccount(userSignUp, SHOP_EMPLOYEE);
    }

    private void registerNewUserAccount(UserSignUp userSignUp, UserRole userRole) throws UserAlreadyExistsException {
        Optional<UserEntity> applicationUser = userRepository.findByUsername(userSignUp.getUsername());
        if (applicationUser.isPresent()) {
            throw new UserAlreadyExistsException("There is an user with that username: " + userSignUp.getUsername());
        }

        String encodePassword = passwordEncoder.encode(userSignUp.getPassword());
        UserEntity userEntity = new UserEntity(userSignUp.getUsername(), encodePassword, userRole);
        userRepository.save(userEntity);
    }
}

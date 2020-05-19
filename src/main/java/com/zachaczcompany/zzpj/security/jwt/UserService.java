package com.zachaczcompany.zzpj.security.jwt;

import com.zachaczcompany.zzpj.security.auth.database.ApplicationUserEntity;
import com.zachaczcompany.zzpj.security.auth.database.ApplicationUserRepository;
import com.zachaczcompany.zzpj.security.configuration.ApplicationUserRole;
import com.zachaczcompany.zzpj.security.jwt.exceptions.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.zachaczcompany.zzpj.security.configuration.ApplicationUserRole.SHOP_EMPLOYEE;
import static com.zachaczcompany.zzpj.security.configuration.ApplicationUserRole.SHOP_OWNER;

@Service
public class UserService {
    private ApplicationUserRepository applicationUserRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewOwner(UserSignUp userSignUp) throws UserAlreadyExistsException {
        registerNewUserAccount(userSignUp, SHOP_OWNER);
    }

    public void registerNewEmployee(UserSignUp userSignUp) throws UserAlreadyExistsException {
        registerNewUserAccount(userSignUp, SHOP_EMPLOYEE);
    }

    private void registerNewUserAccount(UserSignUp userSignUp, ApplicationUserRole applicationUserRole) throws UserAlreadyExistsException {
        Optional<ApplicationUserEntity> applicationUser = applicationUserRepository.findByUsername(userSignUp.getUsername());
        if (applicationUser.isPresent()) {
            throw new UserAlreadyExistsException("There is an user with that username: " + userSignUp.getUsername());
        }

        String encodePassword = passwordEncoder.encode(userSignUp.getPassword());
        ApplicationUserEntity applicationUserEntity = new ApplicationUserEntity(userSignUp.getUsername(), encodePassword, applicationUserRole);
        applicationUserRepository.save(applicationUserEntity);
    }
}

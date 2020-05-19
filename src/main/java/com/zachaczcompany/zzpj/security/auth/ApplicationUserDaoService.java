package com.zachaczcompany.zzpj.security.auth;

import com.zachaczcompany.zzpj.security.auth.database.ApplicationUserEntity;
import com.zachaczcompany.zzpj.security.auth.database.ApplicationUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("database")
public class ApplicationUserDaoService implements ApplicationUserDao {
    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserDaoService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        Optional<ApplicationUserEntity> applicationUserEntityOptional = applicationUserRepository.findByUsername(username);
        return applicationUserEntityOptional.map(ApplicationUserEntity::getApplicationUser);
    }
}

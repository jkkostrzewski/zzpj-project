package com.zachaczcompany.zzpj.security.auth;

import com.zachaczcompany.zzpj.security.auth.database.UserEntity;
import com.zachaczcompany.zzpj.security.auth.database.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("database")
public class UserDaoService implements UserDao {
    private final UserRepository userRepository;

    public UserDaoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> selectApplicationUserByUsername(String username) {
        Optional<UserEntity> applicationUserEntityOptional = userRepository.findByUsername(username);
        return applicationUserEntityOptional.map(UserEntity::getApplicationUser);
    }
}

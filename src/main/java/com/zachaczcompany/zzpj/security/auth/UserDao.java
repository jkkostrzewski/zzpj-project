package com.zachaczcompany.zzpj.security.auth;

import com.zachaczcompany.zzpj.security.auth.database.UserEntity;
import com.zachaczcompany.zzpj.security.auth.database.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository()
public class UserDao implements UserDaoInterface {
    private final UserRepository userRepository;

    public UserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> selectApplicationUserByUsername(String username) {
        return userRepository.findByUsername(username).map(UserEntity::getApplicationUser);
    }
}

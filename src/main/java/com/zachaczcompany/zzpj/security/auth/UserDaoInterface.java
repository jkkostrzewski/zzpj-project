package com.zachaczcompany.zzpj.security.auth;

import java.util.Optional;

public interface UserDaoInterface {
    Optional<User> selectApplicationUserByUsername(String username);
}

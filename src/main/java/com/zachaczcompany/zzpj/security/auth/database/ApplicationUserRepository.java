package com.zachaczcompany.zzpj.security.auth.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends CrudRepository<ApplicationUserEntity,Long> {
    Optional<ApplicationUserEntity> findByUsername(String username);
}

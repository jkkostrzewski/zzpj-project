package com.zachaczcompany.zzpj.security.configuration;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.zachaczcompany.zzpj.security.configuration.UserPermission.QUEUE_WRITE;
import static com.zachaczcompany.zzpj.security.configuration.UserPermission.SHOP_WRITE;


public enum UserRole {
    SHOP_OWNER(Sets.newHashSet(QUEUE_WRITE,SHOP_WRITE)),
    SHOP_EMPLOYEE(Sets.newHashSet(QUEUE_WRITE));

    @Getter
    private final Set<UserPermission> permission;

    UserRole(Set<UserPermission> permission) {
        this.permission = permission;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthority() {
        Set<SimpleGrantedAuthority> permissions = getPermission().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}

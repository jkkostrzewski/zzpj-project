package com.zachaczcompany.zzpj.security.configuration;

import lombok.Getter;

public enum UserPermission {
    SHOP_WRITE("shop:write"),
    QUEUE_WRITE("queue:write");

    @Getter
    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }
}

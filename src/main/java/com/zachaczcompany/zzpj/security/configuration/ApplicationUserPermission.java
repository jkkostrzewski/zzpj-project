package com.zachaczcompany.zzpj.security.configuration;

import lombok.Getter;

public enum ApplicationUserPermission {
    SHOP_WRITE("shop:write"),
    QUEUE_WRITE("queue:write");

    @Getter
    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }
}

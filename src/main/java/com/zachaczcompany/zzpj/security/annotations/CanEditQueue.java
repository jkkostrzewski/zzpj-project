package com.zachaczcompany.zzpj.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority(T(com.zachaczcompany.zzpj.security.configuration.UserPermission).QUEUE_WRITE.permission)")
public @interface CanEditQueue {
}

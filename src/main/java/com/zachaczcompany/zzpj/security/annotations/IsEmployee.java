package com.zachaczcompany.zzpj.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('ROLE_'+T(com.zachaczcompany.zzpj.security.configuration.UserRole).SHOP_EMPLOYEE.name())")
public @interface IsEmployee {
}

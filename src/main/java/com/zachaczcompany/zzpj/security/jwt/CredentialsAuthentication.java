package com.zachaczcompany.zzpj.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialsAuthentication {
    private String username;
    private String password;
}

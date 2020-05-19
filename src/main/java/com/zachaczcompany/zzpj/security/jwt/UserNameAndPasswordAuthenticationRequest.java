package com.zachaczcompany.zzpj.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNameAndPasswordAuthenticationRequest {
    private String username;
    private String password;

    public UserNameAndPasswordAuthenticationRequest() {
    }

}

package com.zachaczcompany.zzpj.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUp {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}

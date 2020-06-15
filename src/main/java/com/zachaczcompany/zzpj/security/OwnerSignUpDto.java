package com.zachaczcompany.zzpj.security;

import com.zachaczcompany.zzpj.security.jwt.UserSignUp;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class OwnerSignUpDto {
    @Valid
    private UserSignUp credentials;

    @Valid
    private ShopCreateDto shop;
}

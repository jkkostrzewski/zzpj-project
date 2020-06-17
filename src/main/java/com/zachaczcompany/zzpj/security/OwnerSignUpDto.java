package com.zachaczcompany.zzpj.security;

import com.zachaczcompany.zzpj.security.jwt.UserSignUp;
import com.zachaczcompany.zzpj.shops.ShopCreateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class OwnerSignUpDto {
    @Schema(description = "Username and password of an owner", required = true)
    @Valid
    private UserSignUp credentials;

    @Schema(description = "Shop information", required = true)
    @Valid
    private ShopCreateDto shop;
}

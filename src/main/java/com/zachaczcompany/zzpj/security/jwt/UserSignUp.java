package com.zachaczcompany.zzpj.security.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "newUser123")
    @NotEmpty
    private String username;

    @Schema(example = "#!newPassword123")
    @NotEmpty
    private String password;
}

package com.zachaczcompany.zzpj.security.jwt;

import com.zachaczcompany.zzpj.security.OwnerSignUpDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/register")
public class RegistrationController {
    private final UserRegistrationService userRegistrationService;

    public RegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @Operation(summary = "Register new owner account", description = "Requires owner credentials and new shop information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful sign up",
                    content = @Content(schema = @Schema(implementation = OwnerSignUpDto.class))),
            @ApiResponse(responseCode = "400", description = "Username has already been used")
    })
    @PostMapping("/owner")
    public ResponseEntity signUpOwner(@RequestBody @Valid OwnerSignUpDto ownerSignUpDto) {
        return userRegistrationService.registerOwner(ownerSignUpDto).toResponseEntity();
    }

    @Operation(summary = "Register new shop employee", description = "Requires username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful sign up"),
            @ApiResponse(responseCode = "400", description = "Username has already been used")
    })
    @PostMapping("/employee")
    public ResponseEntity signUpEmployee(@RequestBody @Valid UserSignUp userSignUp) {
        return userRegistrationService.registerEmployee(userSignUp).toResponseEntity();
    }
}

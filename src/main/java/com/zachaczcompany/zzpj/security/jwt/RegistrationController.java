package com.zachaczcompany.zzpj.security.jwt;

import com.zachaczcompany.zzpj.security.OwnerSignUpDto;
import com.zachaczcompany.zzpj.shops.domain.ShopFacade;
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
    private final ShopFacade shopFacade;

    public RegistrationController(UserRegistrationService userRegistrationService, ShopFacade shopFacade) {
        this.userRegistrationService = userRegistrationService;
        this.shopFacade = shopFacade;
    }

    @PostMapping("/owner")
    public ResponseEntity signUpOwner(@RequestBody @Valid OwnerSignUpDto ownerSignUpDto) {
        return userRegistrationService.registerOwner(ownerSignUpDto).toResponseEntity();
    }

    @PostMapping("/employee")
    public ResponseEntity signUpEmployee(@RequestBody @Valid UserSignUp userSignUp) {
        return userRegistrationService.registerEmployee(userSignUp).toResponseEntity();
    }
}

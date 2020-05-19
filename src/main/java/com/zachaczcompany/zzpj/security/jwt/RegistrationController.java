package com.zachaczcompany.zzpj.security.jwt;

import com.zachaczcompany.zzpj.security.jwt.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {
    UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/owner")
    public ResponseEntity signUpOwner(@RequestBody UserSignUp userSignUp) {
        try {
            userService.registerNewOwner(userSignUp);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/employee")
    public ResponseEntity signUpEmployee(@RequestBody UserSignUp userSignUp) {
        try {
            userService.registerNewEmployee(userSignUp);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

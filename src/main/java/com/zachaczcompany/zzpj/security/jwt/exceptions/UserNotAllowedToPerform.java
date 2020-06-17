package com.zachaczcompany.zzpj.security.jwt.exceptions;

public class UserNotAllowedToPerform extends Exception {
    public UserNotAllowedToPerform(String message) {
        super(message);
    }
}

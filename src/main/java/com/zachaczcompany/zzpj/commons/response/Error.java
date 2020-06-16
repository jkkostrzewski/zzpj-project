package com.zachaczcompany.zzpj.commons.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class Error extends Response<String> {
    private final String code;

    public Error(HttpStatus status, String code) {
        super(status);
        this.code = code;
    }

    public static Error badRequest(String code) {
        return new Error(HttpStatus.BAD_REQUEST, code);
    }

    @Override
    public ResponseEntity<String> toResponseEntity() {
        return new ResponseEntity<>(code, status);
    }
}

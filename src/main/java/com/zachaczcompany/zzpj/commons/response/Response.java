package com.zachaczcompany.zzpj.commons.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Getter
public abstract class Response<T> {
    protected final HttpStatus status;

    public abstract ResponseEntity<T> toResponseEntity();
}

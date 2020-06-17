package com.zachaczcompany.zzpj.commons;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                          HttpHeaders headers, HttpStatus status,
                                                          WebRequest request) {
        return ResponseEntity.badRequest()
                             .body(getBindingErrorResponseBody(exception.getBindingResult()));
    }

    @Override
    protected ResponseEntity handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
                                                 WebRequest request) {
        return ResponseEntity.badRequest()
                             .body(getBindingErrorResponseBody(ex.getBindingResult()));
    }


    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }

    private Map<String, FieldErrorDto> getBindingErrorResponseBody(BindingResult bindingResult) {
        Map<String, FieldErrorDto> errors = new HashMap<>();
        bindingResult.getFieldErrors()
                     .forEach(error -> errors.put(error.getField(), FieldErrorDto.fromFieldError(error)));
        return errors;
    }

    private String getRejectedValueName(ConstraintViolation violation) {
        Iterator<Path.Node> iterator = violation.getPropertyPath()
                                                .iterator();
        Path.Node lastProperty = iterator.hasNext() ? iterator.next() : null;

        while (iterator.hasNext()) {
            lastProperty = iterator.next();
        }

        return (Objects.requireNonNullElse(lastProperty, "")).toString();
    }

    @Getter
    public static class FieldErrorDto {
        private final Object rejectedValue;
        private final String message;

        FieldErrorDto(Object rejectedValue, String message) {
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        static FieldErrorDto fromFieldError(FieldError fieldError) {
            return new FieldErrorDto(fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        }

        static FieldErrorDto fromConstraintViolation(ConstraintViolation violation) {
            return new FieldErrorDto(violation.getInvalidValue(), violation.getMessage());
        }
    }
}

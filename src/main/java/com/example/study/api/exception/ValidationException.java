package com.example.study.api.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ValidationException extends IllegalArgumentException {

    private BindingResult bindingResult;

    public ValidationException() {
        super();
    }

    public ValidationException(String s) {
        super(s);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }
}

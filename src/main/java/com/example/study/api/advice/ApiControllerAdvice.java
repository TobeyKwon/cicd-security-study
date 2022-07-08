package com.example.study.api.advice;

import com.example.study.api.dto.ValidationErrorDto;
import com.example.study.api.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice()
public class ApiControllerAdvice {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorDto> validationErrorHandller(ValidationException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return ResponseEntity.badRequest().body(makeValidationErrorDto(bindingResult));
    }

    public ValidationErrorDto makeValidationErrorDto(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors()
                .stream().forEach(e -> errors.put(((FieldError) e).getField(), e.getDefaultMessage()));
        return new ValidationErrorDto(HttpServletResponse.SC_BAD_REQUEST, errors);
    }
}

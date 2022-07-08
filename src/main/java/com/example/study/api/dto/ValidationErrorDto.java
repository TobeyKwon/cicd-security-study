package com.example.study.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorDto {
    private Integer errorCode;
    private Map<String, String> validationErrorDetail;
}

package com.example.todospring.exception;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ApiError {
    private String message;
    private List<? extends ErrorDetail> errors;

    @Data
    @Builder
    public static class ValidationError implements ErrorDetail {
        private String field;
        private String message;
    }

    @Data
    @Builder
    public static class GeneralError implements ErrorDetail {
        private String message;
    }
}

interface ErrorDetail {}
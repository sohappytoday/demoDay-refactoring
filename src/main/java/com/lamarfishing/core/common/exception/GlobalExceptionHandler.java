package com.lamarfishing.core.common.exception;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ===== BusinessException 처리 =====
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException ex) {
        ErrorCode code = ex.getCode();
        return buildErrorResponse(code.getStatus(), code.getMessage());
    }

    // ===== Response Builder =====
    private ResponseEntity<ApiResponse<?>> buildErrorResponse(HttpStatus status, String message) {
        ApiResponse<?> response = new ApiResponse<>(false, status.toString(), message, null);
        return ResponseEntity.status(status).body(response);
    }
}

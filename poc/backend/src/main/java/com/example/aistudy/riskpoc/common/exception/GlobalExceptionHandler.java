package com.example.aistudy.riskpoc.common.exception;

import com.example.aistudy.riskpoc.common.api.ApiResponse;
import com.example.aistudy.riskpoc.common.error.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(ex.errorCode().httpStatus())
                .body(ApiResponse.error(ex.errorCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidBody(MethodArgumentNotValidException ex) {
        FieldError error = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String message = error == null ? "参数校验失败" : error.getField() + " " + error.getDefaultMessage();
        return validation(message);
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleInvalidParam(Exception ex) {
        return validation("参数校验失败");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        return ResponseEntity.status(ErrorCode.SYSTEM_ERROR.httpStatus())
                .body(ApiResponse.error(ErrorCode.SYSTEM_ERROR, "系统异常"));
    }

    private ResponseEntity<ApiResponse<Void>> validation(String message) {
        return ResponseEntity.status(ErrorCode.VALIDATION_FAILED.httpStatus())
                .body(ApiResponse.error(ErrorCode.VALIDATION_FAILED, message));
    }
}

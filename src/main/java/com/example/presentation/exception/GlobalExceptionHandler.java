package com.example.presentation.exception;

import com.example.application.submission.exception.SubmissionApplicationException;
import com.example.application.user.exception.UserApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SubmissionApplicationException.class)
    public ResponseEntity<ErrorResponse> handleSubmissionApplicationException(SubmissionApplicationException e) {
        ErrorResponse error = new ErrorResponse(
                "SUBMISSION_ERROR",
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(UserApplicationException.class)
    public ResponseEntity<ErrorResponse> handleUserApplicationException(UserApplicationException e) {
        ErrorResponse error = new ErrorResponse(
                "USER_ERROR",
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse error = new ErrorResponse(
                "VALIDATION_ERROR",
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        ErrorResponse error = new ErrorResponse(
                "STATE_ERROR",
                e.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse error = new ErrorResponse(
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.internalServerError().body(error);
    }

    public record ErrorResponse(
            String code,
            String message,
            int status
    ) {}
} 
package com.thiqa.messaging.exception;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@AllArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler {

    private MessageSource messageSource;

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ErrorResponse> handleCustomValidationException(CustomValidationException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse =
                new ErrorResponse(httpStatus, ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse =
                new ErrorResponse(httpStatus, ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class
            , InvalidCsrfTokenException.class, AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ErrorResponse errorResponse =
                new ErrorResponse(httpStatus,
                        messageSource.getMessage("USER.AUTHENTICATION_FAILED", null, Locale.US),
                        LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ErrorResponse errorResponse =
                new ErrorResponse(httpStatus,
                        messageSource.getMessage("USER.NO_PERMISSION", null, Locale.US),
                        LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.info("Exception logs", ex);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse =
                new ErrorResponse(httpStatus, ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}

package com.praveenukkoji.userservice.advice;

import com.praveenukkoji.userservice.dto.Response;
import com.praveenukkoji.userservice.exception.RoleNotFoundException;
import com.praveenukkoji.userservice.exception.UserCreateException;
import com.praveenukkoji.userservice.exception.UserNotFoundException;
import com.praveenukkoji.userservice.exception.UserUpdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserCreateException.class)
    public ResponseEntity<?> handleException(UserCreateException exception) {
        log.error("UserCreateException - {}", exception.getMessage());

        Response response = Response.builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<?> handleException(UserUpdateException exception) {
        log.error("UserUpdateException - {}", exception.getMessage());

        Response response = Response.builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleException(UserNotFoundException exception) {
        log.error("UserNotFoundException - {}", exception.getMessage());

        Response response = Response.builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handleException(RoleNotFoundException exception) {
        log.error("RoleNotFoundException - {}", exception.getMessage());

        Response response = Response.builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(404).body(response);
    }
}

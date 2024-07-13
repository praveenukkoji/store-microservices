package com.praveenukkoji.userservice.advice;

import com.praveenukkoji.userservice.dto.UserResponse;
import com.praveenukkoji.userservice.exception.*;
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

        UserResponse userResponse = UserResponse.builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(400).body(userResponse);
    }

    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<?> handleException(UserUpdateException exception) {
        log.error("UserUpdateException - {}", exception.getMessage());

        UserResponse userResponse = UserResponse.builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(400).body(userResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleException(UserNotFoundException exception) {
        log.error("UserNotFoundException - {}", exception.getMessage());

        UserResponse userResponse = UserResponse.builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(404).body(userResponse);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handleException(RoleNotFoundException exception) {
        log.error("RoleNotFoundException - {}", exception.getMessage());

        UserResponse userResponse = UserResponse.builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(404).body(userResponse);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<?> handleException(AddressNotFoundException exception) {
        log.error("AddressNotFoundException - {}", exception.getMessage());

        UserResponse userResponse = UserResponse.builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(404).body(userResponse);
    }
}

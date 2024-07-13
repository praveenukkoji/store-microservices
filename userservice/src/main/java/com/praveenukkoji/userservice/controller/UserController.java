package com.praveenukkoji.userservice.controller;

import com.praveenukkoji.userservice.dto.UserResponse;
import com.praveenukkoji.userservice.dto.request.CreateUserRequest;
import com.praveenukkoji.userservice.exception.RoleNotFoundException;
import com.praveenukkoji.userservice.exception.UserCreateException;
import com.praveenukkoji.userservice.exception.UserNotFoundException;
import com.praveenukkoji.userservice.exception.UserUpdateException;
import com.praveenukkoji.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest)
            throws UserCreateException, RoleNotFoundException {
        return ResponseEntity.status(201).body(userService.createUser(createUserRequest));
    }

    @GetMapping(path = "/get")
    public ResponseEntity<?> getUser(@RequestParam(defaultValue = "", name = "userId") String userId)
            throws UserNotFoundException {
        if (!Objects.equals(userId, "")) {
            UUID id = UUID.fromString(userId);
            return ResponseEntity.status(200).body(userService.getUser(id));
        }

        UserResponse userResponse = UserResponse.builder()
                .message("user id is empty")
                .build();
        return ResponseEntity.status(400).body(userResponse);
    }

    @PatchMapping(path = "/update")
    public ResponseEntity<?> updateUser(
            @RequestParam(defaultValue = "", name = "userId") String userId,
            @RequestBody Map<String, String> updates
    ) throws UserNotFoundException, UserUpdateException {
        if (!Objects.equals(userId, "")) {
            UUID id = UUID.fromString(userId);
            return ResponseEntity.status(200).body(userService.updateUser(id, updates));
        }

        UserResponse userResponse = UserResponse.builder()
                .message("user id is empty")
                .build();
        return ResponseEntity.status(400).body(userResponse);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> deleteUser(@RequestParam(defaultValue = "", name = "userId") String userId)
            throws UserNotFoundException {
        if (!Objects.equals(userId, "")) {
            UUID id = UUID.fromString(userId);
            return ResponseEntity.status(200).body(userService.deleteUser(id));
        }

        UserResponse userResponse = UserResponse.builder()
                .message("user id is empty")
                .build();
        return ResponseEntity.status(400).body(userResponse);
    }
}

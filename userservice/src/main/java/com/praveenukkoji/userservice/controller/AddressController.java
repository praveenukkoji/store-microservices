package com.praveenukkoji.userservice.controller;

import com.praveenukkoji.userservice.dto.Response;
import com.praveenukkoji.userservice.exception.AddressNotFoundException;
import com.praveenukkoji.userservice.exception.AddressUpdateException;
import com.praveenukkoji.userservice.exception.UserNotFoundException;
import com.praveenukkoji.userservice.model.Address;
import com.praveenukkoji.userservice.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping(path = "/add")
    public ResponseEntity<?> addAddress(
            @RequestParam(defaultValue = "", name = "userId") String userId,
            @RequestBody Address address
    ) throws UserNotFoundException {
        if (!Objects.equals(userId, "")) {
            UUID id = UUID.fromString(userId);
            return ResponseEntity.status(201).body(addressService.addAddress(id, address));
        }

        Response response = Response.builder()
                .message("user id is empty")
                .build();
        return ResponseEntity.status(400).body(response);
    }

    @GetMapping(path = "/get")
    public ResponseEntity<?> getAddress(
            @RequestParam(defaultValue = "", name = "userId") String userId
    ) throws UserNotFoundException {
        if (!Objects.equals(userId, "")) {
            UUID id = UUID.fromString(userId);
            return ResponseEntity.status(200).body(addressService.getAddress(id));
        }

        Response response = Response.builder()
                .message("user id is empty")
                .build();
        return ResponseEntity.status(400).body(response);
    }

    @PatchMapping(path = "/update")
    public ResponseEntity<?> updateUser(
            @RequestParam(defaultValue = "", name = "addressId") String addressId,
            @RequestBody Map<String, String> updates
    ) throws AddressNotFoundException, AddressUpdateException {
        if (!Objects.equals(addressId, "")) {
            UUID id = UUID.fromString(addressId);
            return ResponseEntity.status(200).body(addressService.updateAddress(id, updates));
        }

        Response response = Response.builder()
                .message("address id is empty")
                .build();
        return ResponseEntity.status(400).body(response);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> deleteAddress(
            @RequestParam(defaultValue = "", name = "addressId") String addressId
    ) throws AddressNotFoundException {
        if (!Objects.equals(addressId, "")) {
            UUID id = UUID.fromString(addressId);
            return ResponseEntity.status(200).body(addressService.deleteAddress(id));
        }

        Response response = Response.builder()
                .message("address id is empty")
                .build();
        return ResponseEntity.status(400).body(response);
    }
}

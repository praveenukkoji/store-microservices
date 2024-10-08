package com.praveenukkoji.userservice.service;

import com.praveenukkoji.userservice.dto.request.address.CreateAddressRequest;
import com.praveenukkoji.userservice.dto.response.address.AddressResponse;
import com.praveenukkoji.userservice.exception.address.AddressCreateException;
import com.praveenukkoji.userservice.exception.address.AddressDeleteException;
import com.praveenukkoji.userservice.exception.address.AddressNotFoundException;
import com.praveenukkoji.userservice.exception.address.AddressUpdateException;
import com.praveenukkoji.userservice.exception.user.UserNotFoundException;
import com.praveenukkoji.userservice.model.Address;
import com.praveenukkoji.userservice.model.User;
import com.praveenukkoji.userservice.repository.AddressRepository;
import com.praveenukkoji.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class AddressService {

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    // create
    // TODO: implement isDefault address logic
    public UUID createAddress(CreateAddressRequest createAddressRequest)
            throws UserNotFoundException, AddressCreateException {

        log.info("Creating address: {}", createAddressRequest);

        UUID userId = createAddressRequest.getUserId();
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            Address newAddress = Address.builder()
                    .line(createAddressRequest.getLine())
                    .country(createAddressRequest.getCountry())
                    .state(createAddressRequest.getState())
                    .city(createAddressRequest.getCity())
                    .pincode(createAddressRequest.getPincode())
                    .isDefault(false)
                    .user(user.get())
                    .build();

            try {
                return addressRepository.save(newAddress).getId();
            } catch (Exception e) {
                throw new AddressCreateException(e.getMessage());
            }
        }

        throw new UserNotFoundException("user with id = " + userId + " not found");
    }

    // retrieve
    public AddressResponse getAddress(UUID addressId)
            throws AddressNotFoundException {

        log.info("Getting address: {}", addressId);

        Optional<Address> address = addressRepository.findById(addressId);

        if (address.isPresent()) {

            return AddressResponse.builder()
                    .id(address.get().getId())
                    .line(address.get().getLine())
                    .country(address.get().getCountry())
                    .state(address.get().getState())
                    .city(address.get().getCity())
                    .pincode(address.get().getPincode())
                    .isDefault(address.get().getIsDefault())
                    .build();
        }

        throw new AddressNotFoundException("address with id = " + addressId + " not found");

    }

    // update
    // TODO: change Map<String, String> to Class of UpdateAddressRequest
    public UUID updateAddress(UUID addressId, Map<String, String> updates)
            throws AddressNotFoundException, AddressUpdateException {

        log.info("Updating address: {}", updates);

        Optional<Address> address = addressRepository.findById(addressId);

        if (address.isPresent()) {
            Address updatedAddress = address.get();

            for (Map.Entry<String, String> entry : updates.entrySet()) {
                switch (entry.getKey()) {
                    case "line":
                        if (!Objects.equals(entry.getValue(), "")) {
                            updatedAddress.setLine(entry.getValue());
                        }
                        break;
                    case "country":
                        if (!Objects.equals(entry.getValue(), "")) {
                            updatedAddress.setCountry(entry.getValue());
                        }
                        break;
                    case "state":
                        if (!Objects.equals(entry.getValue(), "")) {
                            updatedAddress.setState(entry.getValue());
                        }
                        break;
                    case "city":
                        if (!Objects.equals(entry.getValue(), "")) {
                            updatedAddress.setCity(entry.getValue());
                        }
                        break;
                    case "pincode":
                        if (!Objects.equals(entry.getValue(), "")) {
                            updatedAddress.setPincode(entry.getValue());
                        }
                        break;
                }
            }

            try {
                return addressRepository.save(updatedAddress).getId();
            } catch (Exception e) {
                throw new AddressUpdateException(e.getMessage());
            }
        }

        throw new AddressNotFoundException("address with id = " + addressId + " not found");
    }

    // delete
    public void deleteAddress(UUID addressId)
            throws AddressNotFoundException, AddressDeleteException {

        log.info("Deleting address: {}", addressId);

        Optional<Address> address = addressRepository.findById(addressId);

        if (address.isPresent()) {

            // if address is default cannot delete it
            if (address.get().getIsDefault()) {
                throw new AddressDeleteException("cannot delete default address");
            }

            try {
                addressRepository.deleteById(addressId);
                return;
            } catch (Exception e) {
                throw new AddressDeleteException(e.getMessage());
            }
        }

        throw new AddressNotFoundException("address with id = " + addressId + " not found");
    }

    // get address by user
    public List<AddressResponse> getAddressByUser(UUID userId) throws UserNotFoundException {

        log.info("Getting addresses by user: {}", userId);
        
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            List<Address> addressList = addressRepository.findAllByUser(user.get());

            return addressList.stream().map(address -> {
                return AddressResponse.builder()
                        .id(address.getId())
                        .line(address.getLine())
                        .country(address.getCountry())
                        .state(address.getState())
                        .city(address.getCity())
                        .pincode(address.getPincode())
                        .isDefault(address.getIsDefault())
                        .build();
            }).toList();
        }

        throw new UserNotFoundException("user with id = " + userId + " not found");
    }
}

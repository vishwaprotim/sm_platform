package com.protim.service.user.service;

import com.protim.service.user.dto.AddressDto;
import com.protim.service.user.dto.UserProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserProfileDto createUser(UserProfileDto user);
    UserProfileDto getUser(String userName);
    UserProfileDto updateUser(UserProfileDto user);
    UserProfileDto suspendUser(String userName);
    Page<String> getUserNames(String status, Pageable pageable);

    AddressDto addAddress(AddressDto address);
    AddressDto deleteAddress(String userName, String addressId);
    AddressDto getPrimaryAddress(String userName);
    AddressDto setPrimaryAddress(String userName, String addressId);
    List<AddressDto> getAllAddress(String userName);

}

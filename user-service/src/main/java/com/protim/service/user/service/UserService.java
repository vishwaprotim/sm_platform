package com.protim.service.user.service;

import com.protim.service.user.dto.AddressDto;
import com.protim.service.user.dto.UserProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserProfileDto createUser(UserProfileDto user);
    UserProfileDto getUser(UUID userUUID);
    UserProfileDto getUser(String userName);
    Page<String> getUserNames(String status, Pageable pageable);
    UserProfileDto updateUser(UserProfileDto user);
    UserProfileDto suspendUser(String userName);
    AddressDto addAddress(String userName, AddressDto address);
    AddressDto getPrimaryAddress(String userName);
    List<AddressDto> getAllAddress(String userName);

}

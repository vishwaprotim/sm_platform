package com.protim.service.user.controller;


import com.protim.service.user.api.BaseResponse;
import com.protim.service.user.api.UserApi;
import com.protim.service.user.dto.AddressDto;
import com.protim.service.user.dto.UserProfileDto;
import com.protim.service.user.dto.UserProfileUpdateDto;
import com.protim.service.user.enums.Status;
import com.protim.service.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileDto createUser(@Valid @RequestBody UserProfileDto user){
        return userService.createUser(user);
    }

    @Override
    @GetMapping("/ids")
    @ResponseStatus(HttpStatus.OK)
    public Page<String> getAllUserNamesByStatus(
            @RequestParam(required = false) String status,
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return ((status != null && !status.isBlank()) && !Status.isValidStatus(status))?
                Page.empty(pageable):
                userService.getUserNames(status, pageable);
    }

    @Override
    @GetMapping("/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileDto getUser(@PathVariable("userName") String userName){
        return userService.getUser(userName);
    }

    @Override
    @PatchMapping("/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileDto updateUser(
            @PathVariable("userName") String userName,
            @Valid @RequestBody UserProfileUpdateDto updateDto){
        return userService.updateUser(userName, updateDto);
    }

    @Override
    @PatchMapping("/{userName}/suspend")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileDto suspendUser(@PathVariable("userName") String userName){
        return userService.updateUserStatus(userName, Status.SUSPENDED);
    }

    @Override
    @PatchMapping("/{userName}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileDto deactivateUser(@PathVariable("userName") String userName){
        return userService.updateUserStatus(userName, Status.DEACTIVATED);
    }

    @Override
    @PatchMapping("/{userName}/restore")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileDto restoreUser(@PathVariable("userName") String userName){
        return userService.updateUserStatus(userName, Status.ACTIVE);
    }

    @Override
    @PostMapping("/address")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDto addAddress(@Valid @RequestBody AddressDto addressDto){
        return userService.addAddress(addressDto);
    }

    @Override
    @GetMapping("/{userName}/address")
    @ResponseStatus(HttpStatus.OK)
    public List<AddressDto> getAddressesForUser(@PathVariable("userName") String userName){
        return userService.getAllAddress(userName);
    }

    @Override
    @GetMapping("/{userName}/address/primary")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getPrimaryAddressesForUser(@PathVariable("userName") String userName){
        return userService.getPrimaryAddress(userName);
    }

    @Override
    @DeleteMapping("/{userName}/address/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteAddress(@PathVariable("userName") String userName,
                                      @PathVariable("addressId") String addressId){
        var deletedResource = userService.deleteAddress(userName, addressId);
        return BaseResponse.builder()
                .status(HttpStatus.OK)
                .message("DELETED: Address " + deletedResource.getId())
                .build();
    }

    @Override
    @PatchMapping("/{userName}/address/{addressId}/set-primary")
    public BaseResponse setPrimary(@PathVariable("userName") String userName,
                                 @PathVariable("addressId") String addressId){
        var updatedResource = userService.setPrimaryAddress(userName, addressId);
        return BaseResponse.builder()
                .status(HttpStatus.OK)
                .message("Address " + updatedResource.getId() + " set primary for user " + userName)
                .build();
    }
}

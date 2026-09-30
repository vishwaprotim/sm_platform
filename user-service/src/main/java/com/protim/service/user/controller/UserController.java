package com.protim.service.user.controller;


import com.protim.service.user.api.UserApi;
import com.protim.service.user.dto.UserProfileDto;
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

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @GetMapping("/ids")
    Page<String> getAllUserNamesByStatus(
            @RequestParam String status,
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        // Returns users filtered by status
        // if no status is provided, return all users
        // if bad status is provided return empty list
        // Sample URL: /api/v1/user/ids?status=active&page=2&size=10&sort=id,asc
        if((status != null && !status.isBlank()) && !Status.isValidStatus(status)){
            return Page.empty(pageable);
        }
        return userService.getUserNames(status, pageable);
    }

    @GetMapping("/{userName}")
    @ResponseStatus(HttpStatus.OK)
    UserProfileDto getUser(@PathVariable("userName") String userName){
        return userService.getUser(userName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileDto createUser(@Valid @RequestBody UserProfileDto user){
        return userService.createUser(user);
    }
}

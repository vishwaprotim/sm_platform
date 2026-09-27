package com.protim.service.user.service;

import com.protim.service.user.entity.Address;
import org.apache.catalina.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(User user);
    User getUser(UUID userUUID);
    User getUser(String userId);
    User updateUser(User user);
    User suspendUser(String userId);
    Address addAddress(Address address);
    Address getPrimaryAddress(UUID userUUID);
    List<Address> getAllAddress(UUID userUUID);

}

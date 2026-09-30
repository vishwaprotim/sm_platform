package com.protim.service.user.service.impl;

import com.protim.service.user.dto.AddressDto;
import com.protim.service.user.enums.Status;
import com.protim.service.user.exception.BadRequestException;
import com.protim.service.user.exception.ResourceNotFoundException;
import com.protim.service.user.dto.UserProfileDto;
import com.protim.service.user.repository.AddressRepository;
import com.protim.service.user.repository.UserProfileRepository;
import com.protim.service.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserProfileRepository userProfileRepository;
    private final AddressRepository addressRepository;

    // TODO : This will be cached
    Set<String> takenUserNames(){
        List<String> takenUserNames = userProfileRepository.findAllUserNames();
        return new HashSet<>(takenUserNames);
    }

    /**
     * Creates a new user profile
     * @param user User Profile Request
     * @return Returns the DTO of the entity created
     */
    @Override
    @Transactional
    public UserProfileDto createUser(UserProfileDto user) {
        var userProfileEntity = user.entity();
        if(takenUserNames().contains(userProfileEntity.getUserName())){
            throw new BadRequestException("UserName " + userProfileEntity.getUserName() + " is already taken.");
        }

        userProfileEntity.setStatus(Status.ACTIVE);
        userProfileEntity.setCreatedAt(Instant.now());
        userProfileEntity.setUpdatedAt(Instant.now());
        var savedEntity = userProfileRepository.save(userProfileEntity);

        var addressEntity = user.primaryAddressEntity();
        addressEntity.setUserUUID(userProfileEntity.getId());
        addressEntity.setPrimary(true);
        addressEntity.setCreatedAt(Instant.now());
        addressEntity.setUpdatedAt(Instant.now());
        var savedAddressEntity = addressRepository.save(addressEntity);

        return UserProfileDto.fromEntity(savedEntity, savedAddressEntity);
    }

    @Override
    public UserProfileDto getUser(UUID userUUID) {
        var userEntity = userProfileRepository.findById(userUUID)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with UUID: " + userUUID));

        var addressEntity = addressRepository.findByUserUUIDAndIsPrimary(userUUID, true)
                .orElseGet(() -> {
                    log.warn("Primary address not found for user UUID: {}", userUUID);
                    return null;
                });
        return UserProfileDto.fromEntity(userEntity, addressEntity);
    }

    @Override
    public Page<String> getUserNames(String status, Pageable pageable){
        if(status == null || status.isBlank()){
            return userProfileRepository.findAllUserNames(pageable);
        }

        return userProfileRepository.findUserNamesByStatus(Status.fromString(status), pageable);
    }

    @Override
    public UserProfileDto getUser(String userName) {
        var entity = userProfileRepository.findByUserName(userName)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User " + userName + " does not exist"));
        var addressEntity = addressRepository.findByUserUUIDAndIsPrimary(entity.getId(), true)
                .orElseGet(() -> {
                    log.warn("Primary address not found for user: {}", userName);
                    return null;
                });

        return UserProfileDto.fromEntity(entity, addressEntity);
    }

    @Override
    public UserProfileDto updateUser(UserProfileDto user) {
        throw new UnsupportedOperationException("Method not implemented yet!");
    }

    @Override
    public UserProfileDto suspendUser(String userName) {
        throw new UnsupportedOperationException("Method not implemented yet!");
    }

    @Override
    public AddressDto addAddress(String userName, AddressDto address) {
        // check if user with this userName exists, else throw user not found error
        UUID userUUID = userProfileRepository.findUserUUIDByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userName + " does not exist"));

        // check if primary address is already present
        if(address.getIsPrimary()){
            var existingPrimaryAddress = addressRepository.findByUserUUIDAndIsPrimary(userUUID, true);
            if(existingPrimaryAddress.isPresent()){
                throw new BadRequestException("Primary address is already present for user " + userName);
            }
        }

        var entity = address.toEntity();
        entity.setUserUUID(userUUID);
        return AddressDto.fromEntity(addressRepository.save(entity));
    }

    @Override
    public AddressDto getPrimaryAddress(String userName) {
        UUID userUUID = userProfileRepository.findUserUUIDByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userName + " does not exist"));

        var entity = addressRepository.findByUserUUIDAndIsPrimary(userUUID, true)
                .orElseThrow(() -> new ResourceNotFoundException("No primary address found for userUUID: " + userUUID)
        );

        return AddressDto.fromEntity(entity);
    }

    @Override
    public List<AddressDto> getAllAddress(String userName) {
        UUID userUUID = userProfileRepository.findUserUUIDByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userName + " does not exist"));

        var entities = addressRepository.findByUserUUID(userUUID);
        return entities.stream().map(AddressDto::fromEntity).toList();
    }
}

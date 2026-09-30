package com.protim.service.user.service.impl;

import com.protim.service.user.dto.AddressDto;
import com.protim.service.user.entity.Address;
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
import java.util.*;

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
        return UserProfileDto.fromEntity(
                entity,
                addressRepository.findByUserUUIDAndIsDeletedFalseOrderByUpdatedAtDesc(entity.getId()));
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
    public AddressDto addAddress(AddressDto address) {
        String userName = address.getUserName();
        if(userName == null || userName.isBlank()){
            throw new BadRequestException("Username is required");
        }

        if(address.getIsPrimary() == null){
            address.setIsPrimary(false);
        }

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
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        var savedDto = AddressDto.fromEntity(addressRepository.save(entity));
        savedDto.setUserName(userName);
        return savedDto;
    }

    @Override
    public AddressDto getPrimaryAddress(String userName) {
        UUID userUUID = userProfileRepository.findUserUUIDByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userName + " does not exist"));

        var entity = addressRepository.findByUserUUIDAndIsPrimary(userUUID, true)
                .orElseThrow(() -> new ResourceNotFoundException("No primary address found for userUUID: " + userUUID)
        );

        var dto = AddressDto.fromEntity(entity);
        dto.setUserName(userName);
        return dto;
    }

    @Override
    public List<AddressDto> getAllAddress(String userName) {
        UUID userUUID = userProfileRepository.findUserUUIDByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userName + " does not exist"));

        var entities = addressRepository.findByUserUUIDAndIsDeletedFalseOrderByUpdatedAtDesc(userUUID);
        var dtoList = entities.stream().map(AddressDto::fromEntity).toList();
        dtoList.forEach(item -> item.setUserName(userName));
        return dtoList;
    }

    @Override
    public AddressDto deleteAddress(String userName, String addressId){
        UUID userUUID = userProfileRepository.findUserUUIDByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userName + " does not exist"));

        var entities = addressRepository
                .findByUserUUIDAndIsDeletedFalseOrderByUpdatedAtDesc(userUUID);

        var addressToDelete = entities.stream()
                .filter(e -> addressId.equals(e.getId().toString()))
                .findFirst()
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Address " + addressId + " for user " + userName + " not found"));

        // You cannot delete the primary address
        if(addressToDelete.isPrimary()){
            throw new BadRequestException("Cannot delete primary address");
        }

        addressToDelete.setDeleted(true);
        addressToDelete.setDeletedAt(Instant.now());
        addressRepository.save(addressToDelete);

        return AddressDto.fromEntity(addressToDelete);
    }


    @Override
    public AddressDto setPrimaryAddress(String userName, String addressId){
        UUID userUUID = userProfileRepository.findUserUUIDByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userName + " does not exist"));

        var entities = addressRepository
                .findByUserUUIDAndIsDeletedFalseOrderByUpdatedAtDesc(userUUID);

        var addressToUpdate = entities.stream()
                .filter(e -> addressId.equals(e.getId().toString()))
                .findFirst()
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Address " + addressId + " for user " + userName + " not found"));

        if(addressToUpdate.isPrimary()){
            return AddressDto.fromEntity(addressToUpdate); // idempotent update
        }

        List<Address> entitiesToUpdate = new ArrayList<>();
        // Set the existing primary address to non-primary
        var existingPrimaryAddressSearch = entities.stream()
                .filter(Address::isPrimary)
                .findFirst();
        if(existingPrimaryAddressSearch.isPresent()){
            var existingPrimaryAddress = existingPrimaryAddressSearch.get();
            existingPrimaryAddress.setPrimary(false);
            existingPrimaryAddress.setUpdatedAt(Instant.now());
            entitiesToUpdate.add(existingPrimaryAddress);
        }

        // Set this address as primary
        addressToUpdate.setPrimary(true);
        addressToUpdate.setUpdatedAt(Instant.now());
        entitiesToUpdate.add(addressToUpdate);

        addressRepository.saveAll(entitiesToUpdate);
        return AddressDto.fromEntity(addressToUpdate);
    }


}

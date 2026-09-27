package com.protim.service.user.dto;

import com.protim.service.user.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    UUID id;
    String userId;
    String addressType;
    boolean isPrimary;

    String addressLine1;
    String addressLine2;
    String addressLine3;
    String zipCode;
    String city;
    String state;
    String country;

    Instant createdAt;
    Instant updatedAt;

    public Address toEntity(){
        Address address = new Address();
        address.setId(id);
        address.setUserId(userId);
        address.setPrimary(isPrimary);

        address.setAddressLine1(addressLine1);
        address.setAddressLine2(addressLine2);
        address.setAddressLine3(addressLine3);
        address.setZipCode(zipCode);
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);

        address.setCreatedAt(createdAt);
        address.setUpdatedAt(updatedAt);
        return address;
    }

    public static AddressDto fromEntity(Address entity){
        return AddressDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .isPrimary(entity.isPrimary())
                .addressLine1(entity.getAddressLine1())
                .addressLine2(entity.getAddressLine2())
                .addressLine3(entity.getAddressLine3())
                .zipCode(entity.getZipCode())
                .city(entity.getCity())
                .state(entity.getState())
                .country(entity.getCountry())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

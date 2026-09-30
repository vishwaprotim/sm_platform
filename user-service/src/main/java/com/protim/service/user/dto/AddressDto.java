package com.protim.service.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.protim.service.user.entity.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL) // Skips all null fields for this DTO
public class AddressDto {

    @NotBlank(message = "Address type cannot be empty")
    String addressType;

    @JsonProperty("isPrimary")
    Boolean isPrimary = false;

    @NotBlank(message = "Address cannot be empty")
    String addressLine1;

    String addressLine2;

    String addressLine3;

    @NotBlank(message = "ZIP Code is required")
    @Size(min = 3, max = 6, message = "ZIP Code must be between 3 to 6 characters")
    @Pattern(regexp = "^[0-9]+$", message = "ZIP Code can only contain digits")
    String zipCode;

    @NotBlank(message = "City cannot be empty")
    String city;

    @NotBlank(message = "State cannot be empty")
    String state;

    @NotBlank(message = "Country cannot be empty")
    String country;

    // Populated by system
    // Fields marked as READ_ONLY so they are sent in API responses but ignored if sent in API requests
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UUID userUUID;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Instant createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Instant updatedAt;

    public Address toEntity(){
        Address address = new Address();
        address.setId(id);
        address.setUserUUID(userUUID);
        address.setPrimary(isPrimary);
        address.setAddressType(addressType);

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
        if(entity == null){
            return null;
        }

        return AddressDto.builder()
                .id(entity.getId())
                .userUUID(entity.getUserUUID())
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

package com.protim.service.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.protim.service.user.entity.Address;
import com.protim.service.user.entity.UserProfile;
import com.protim.service.user.enums.Status;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;


@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL) // Skips all null fields for this DTO
public class UserProfileDto {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters and must not contain spaces")
    @Pattern(regexp = "^\\S+$", message = "Username cannot contain any blank spaces")
    String userName;

    @NotBlank(message = "Password cannot be blank")
    String passwordHash;

    @NotBlank(message = "First Name cannot be empty")
    String firstName;

    String middleName;

    @NotBlank(message = "Last Name cannot be empty")
    String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") // handled by GlobalExceptionHandler
    LocalDate dateOfBirth;

    @NotBlank(message = "Contact number is required")
    @Size(min = 5, message = "Contact number must be greater than 4 characters")
    @Pattern(regexp = "^[0-9+\\-]+$", message = "Contact number can only contain digits, '+' and '-'")
    String contactNumber;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email address")
    String email;

    String bio;

    @NotNull(message = "Address is required")
    AddressDto primaryAddress;

    // Populated by System
    // Fields marked as READ_ONLY so they are sent in API responses but ignored if sent in API requests
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Status status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Instant createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Instant updatedAt;

    // Derived field
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long age;

    List<AddressDto> address;

    public UserProfile entity(){
        UserProfile userProfile = new UserProfile();
        userProfile.setId(id);
        userProfile.setUserName(userName);
        userProfile.setPasswordHash(encrypt(passwordHash));
        userProfile.setStatus(status);

        userProfile.setFirstName(firstName);
        userProfile.setMiddleName(middleName);
        userProfile.setLastName(lastName);

        userProfile.setDateOfBirth(dateOfBirth);
        userProfile.setContactNumber(contactNumber);
        userProfile.setEmail(email);
        userProfile.setBio(bio);

        userProfile.setCreatedAt(createdAt);
        userProfile.setUpdatedAt(updatedAt);
        return userProfile;
    }

    public Address primaryAddressEntity(){
        return primaryAddress.toEntity();
    }

    public static UserProfileDto fromEntity(UserProfile entity){
        if(entity == null){
            return null;
        }

        var dto = UserProfileDto.builder()
                .id(entity.getId())
                .userName(entity.getUserName())
                .status(entity.getStatus())
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .lastName(entity.getLastName())
                .dateOfBirth(entity.getDateOfBirth())
                .contactNumber(entity.getContactNumber())
                .email(entity.getEmail())
                .bio(entity.getBio())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

        // derived fields
        dto.setAge(ChronoUnit.YEARS.between(dto.getDateOfBirth(), LocalDate.now()));
        return dto;
    }

    public static UserProfileDto fromEntity(UserProfile entity, Address primaryAddress){
        if(entity == null){
            return null;
        }

        var dto = UserProfileDto.fromEntity(entity);
        dto.setPrimaryAddress(AddressDto.fromEntity(primaryAddress));
        return dto;
    }

    public static UserProfileDto fromEntity(UserProfile entity, List<Address> addressEntityList){
        if(entity == null){
            return null;
        }

        var dto = UserProfileDto.fromEntity(entity);
        var addressList = addressEntityList.stream().map(AddressDto::fromEntity).toList();
        dto.setAddress(addressList);
        return dto;
    }

    // TODO - integrate auth for all endpoints except register
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public String encrypt(String rawPassword) {
        return (rawPassword == null || rawPassword.isBlank())?
                rawPassword :
                PASSWORD_ENCODER.encode(rawPassword);
    }
}

package com.protim.service.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.protim.service.user.entity.UserProfile;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.util.stream.Stream;


@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL) // Skips all null fields for this DTO
public class UserProfileUpdateDto {

    String firstName;
    String middleName;
    String lastName;

    @Past(message = "Date of birth must be in the past")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") // handled by GlobalExceptionHandler
    LocalDate dateOfBirth;

    @Size(min = 5, message = "Contact number must be greater than 4 characters")
    @Pattern(regexp = "^[0-9+\\-]+$", message = "Contact number can only contain digits, '+' and '-'")
    String contactNumber;

    @Email(message = "Please provide a valid email address")
    String email;

    String bio;

    public void applyPatchToEntity(UserProfile existingEntity){
        if(firstName != null && !firstName.isBlank()) existingEntity.setFirstName(firstName);
        if(middleName != null && !middleName.isBlank()) existingEntity.setMiddleName(middleName);
        if(lastName != null && !lastName.isBlank()) existingEntity.setLastName(lastName);

        if(dateOfBirth != null) existingEntity.setDateOfBirth(dateOfBirth);
        if(contactNumber != null) existingEntity.setContactNumber(contactNumber);
        if(email != null) existingEntity.setEmail(email);
        if(bio != null && !bio.isBlank()) existingEntity.setBio(bio);

        existingEntity.setUpdatedAt(Instant.now());
    }

    public boolean hasUpdates(){
        boolean hasStrings = Stream.of(firstName, middleName, lastName, contactNumber, email, bio)
                .anyMatch(val -> val != null && !val.isBlank());

        boolean hasObjects = (dateOfBirth != null);

        return (hasStrings || hasObjects);
    }

}

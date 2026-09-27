package com.protim.service.user.dto;

import com.protim.service.user.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    UUID id;
    String userName; // must be unique
    String passwordHash;

    String firstName;
    String middleName;
    String lastName;

    LocalDate dateOfBirth;
    long age;
    String contactNumber;
    String email;
    String bio;

    Instant createdAt;
    Instant updatedAt;

    public UserProfile toEntity(){
        UserProfile userProfile = new UserProfile();
        userProfile.setId(id);
        userProfile.setUserName(userName);
        userProfile.setPasswordHash(passwordHash);

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

    public static UserProfileDto fromEntity(UserProfile entity){
        var dto = UserProfileDto.builder()
                .id(entity.getId())
                .userName(entity.getUserName())
                .passwordHash(entity.getPasswordHash())
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
}

package com.protim.service.user.entity;


import jakarta.persistence.Entity;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
public class UserProfile {

    UUID id;
    String userName; // must be unique
    String passwordHash;

    String firstName;
    String middleName;
    String lastName;

    LocalDate dateOfBirth;
    String contactNumber;
    String email;
    String bio;

    Instant createdAt;
    Instant updatedAt;
}

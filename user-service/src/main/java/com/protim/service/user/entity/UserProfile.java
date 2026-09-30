package com.protim.service.user.entity;


import com.protim.service.user.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;


@Data
@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String firstName;
    private String middleName;
    private String lastName;

    private LocalDate dateOfBirth;
    private String contactNumber;

    private String email;

    private String bio;

    private Instant createdAt;
    private Instant updatedAt;
}

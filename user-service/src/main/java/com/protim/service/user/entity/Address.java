package com.protim.service.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    UUID userUUID;

    @Column(nullable = false)
    String addressType;

    @Column(nullable = false)
    boolean isPrimary;

    @Column(nullable = false)
    String addressLine1;

    String addressLine2;

    String addressLine3;

    @Column(nullable = false)
    String zipCode;

    @Column(nullable = false)
    String city;

    @Column(nullable = false)
    String state;

    @Column(nullable = false)
    String country;

    Instant createdAt;
    Instant updatedAt;

    Instant deletedAt;
    boolean isDeleted;
}

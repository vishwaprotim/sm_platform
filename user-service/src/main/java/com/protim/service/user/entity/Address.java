package com.protim.service.user.entity;

import jakarta.persistence.Entity;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
public class Address {

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
}

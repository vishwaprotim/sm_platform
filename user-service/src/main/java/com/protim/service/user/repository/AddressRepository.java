package com.protim.service.user.repository;

import com.protim.service.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    Optional<Address> findByUserUUIDAndIsPrimary(UUID userUUID, boolean isPrimary);

    List<Address> findByUserUUID(UUID userUUID);
}

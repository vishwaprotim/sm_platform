package com.protim.service.user.repository;

import com.protim.service.user.entity.UserProfile;
import com.protim.service.user.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByUserName(String userName);

    @Query("SELECT u.userName FROM UserProfile u WHERE u.status = :status")
    Page<String> findUserNamesByStatus(@Param("status") Status status, Pageable pageable);

    @Query("SELECT u.userName FROM UserProfile u")
    Page<String> findAllUserNames(Pageable pageable);

    @Query("SELECT u.userName FROM UserProfile u")
    List<String> findAllUserNames();

    @Query("SELECT u.id FROM UserProfile u WHERE u.userName = :userName")
    Optional<UUID> findUserUUIDByUserName(@Param("userName") String userName);
}

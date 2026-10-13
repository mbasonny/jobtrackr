package com.jobtrackr.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ApplicationRepository extends JpaRepository<JobApplicationEntity, UUID> {

    List<JobApplicationEntity> findByUserIdOrderByAppliedDateDesc(UUID userId);

    List<JobApplicationEntity> findByUserIdAndStatusOrderByAppliedDateDesc(UUID userId, Status status);

    Optional<JobApplicationEntity> findByIdAndUserId(UUID id, UUID userId);

    long countByUserIdAndStatus(UUID userId, Status status);
}

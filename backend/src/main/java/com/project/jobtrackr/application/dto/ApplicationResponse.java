package com.project.jobtrackr.application.dto;

import com.project.jobtrackr.application.Status;

import java.time.LocalDate;
import java.util.UUID;

public record ApplicationResponse(
        UUID id,
        String company,
        String position,
        String url,
        String notes,
        Status status,
        LocalDate appliedDate
) {
}

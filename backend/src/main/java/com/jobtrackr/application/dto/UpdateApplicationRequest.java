package com.jobtrackr.application.dto;

import com.jobtrackr.application.Status;

import java.time.LocalDate;

public record UpdateApplicationRequest(
        String company,
        String position,
        String url,
        String notes,
        Status status,
        LocalDate appliedDate
) {
}

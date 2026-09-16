package com.project.jobtrackr.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateApplicationRequest(
        @NotBlank(message = "L'entreprise est requise")
        String company,

        @NotBlank(message = "Le poste est requise")
        String position,

        String url,

        String notes,

        @NotNull(message = "La date de candidature est requise")
        LocalDate appliedDate
) {
}

package com.project.jobtrackr.application.dto;

public record StatsResponse(
        long postule,
        long entretien,
        long refuse,
        long offre,
        long total
) {
}

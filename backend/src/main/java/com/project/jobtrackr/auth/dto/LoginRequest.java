package com.project.jobtrackr.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginRequest(
        @NotBlank @Email(message="Adresse courriel invalide")
        String email,

        @NotBlank(message="Le mot de passe est requis")
        String password
)
{}

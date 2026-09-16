package com.project.jobtrackr.auth.dto;

public record AuthResponse(
    String token,
    String email,
    String fullName
){}

package com.jobtrackr.auth.dto;

public record AuthResponse(
    String token,
    String email,
    String fullName
){}

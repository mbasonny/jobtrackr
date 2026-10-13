package com.jobtrackr.auth;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name="full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.USER;

}

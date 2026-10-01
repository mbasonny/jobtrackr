package com.jobtrackr.auth.service;
import com.jobtrackr.auth.Role;
import com.jobtrackr.auth.User;
import com.jobtrackr.auth.UserRepository;
import com.jobtrackr.auth.dto.AuthResponse;
import com.jobtrackr.auth.dto.LoginRequest;
import com.jobtrackr.auth.dto.RegisterRequest;
import com.jobtrackr.auth.security.CustomUserDetails;
import com.jobtrackr.common.EmailAlreadyUsedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        log.info("Vérification de l'existance de l'adresse email");
        if(userRepository.existsByEmail(request.email())){
            throw new EmailAlreadyUsedException(request.email());
        }
        log.info("Ajout du nouvel user");
        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        log.info("Construction du jwt et retour des des informations");
        String token = jwtService.generateToken(new CustomUserDetails(user));
        return new AuthResponse(token, user.getEmail(), user.getFullName());
    }


    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new IllegalStateException("Utilisateur introuvable")
        );

        String token = jwtService.generateToken(new CustomUserDetails(user));
        return new AuthResponse(token, request.email(), request.password());
    }
}

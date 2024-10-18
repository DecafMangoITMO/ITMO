package com.decafmango.lab_1.user.service;

import com.decafmango.lab_1.exception.exceptions.UserAlreadyExistsException;
import com.decafmango.lab_1.exception.exceptions.UserNotFoundException;
import com.decafmango.lab_1.security.JwtService;
import com.decafmango.lab_1.user.dao.UserRepository;
import com.decafmango.lab_1.user.dto.AuthResponseDto;
import com.decafmango.lab_1.user.dto.LoginUserDto;
import com.decafmango.lab_1.user.dto.RegisterUserDto;
import com.decafmango.lab_1.user.model.Role;
import com.decafmango.lab_1.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegisterUserDto registerUserDto) {
        if (userRepository.existsByUsername(registerUserDto.getUsername()))
            throw new UserAlreadyExistsException(
                    String.format("Username %s already exists", registerUserDto.getUsername())
            );

        User user = User
                .builder()
                .username(registerUserDto.getUsername())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .role(Role.USER)
                .build();

        user = userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponseDto(
                user.getUsername(),
                user.getRole(),
                token
        );
    }

    public AuthResponseDto login(LoginUserDto loginUserDto) {
        User user = userRepository.findByUsername(loginUserDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Username %s not found", loginUserDto.getUsername())
                ));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(), loginUserDto.getPassword())
        );

        String token = jwtService.generateToken(user);
        return new AuthResponseDto(
                user.getUsername(),
                user.getRole(),
                token
        );
    }

}

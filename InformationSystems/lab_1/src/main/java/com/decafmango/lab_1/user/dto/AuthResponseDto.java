package com.decafmango.lab_1.user.dto;

import com.decafmango.lab_1.user.model.Role;
import lombok.Getter;

@Getter
public class AuthResponseDto {
    private String username;
    private Role role;
    private String token;
    private final String tokenType = "Bearer ";

    public AuthResponseDto(String username, Role role, String token) {
        this.username = username;
        this.role = role;
        this.token = token;
    }
}

package com.decafmango.lab_1.user.dto;

import com.decafmango.lab_1.user.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class UserDto {
    private Long id;
    private String username;
    private Role role;
}

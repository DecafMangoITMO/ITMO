package com.decafmango.lab_1.user.service;

import com.decafmango.lab_1.security.JwtService;
import com.decafmango.lab_1.user.dao.UserRepository;
import com.decafmango.lab_1.user.dto.UserDto;
import com.decafmango.lab_1.user.model.Role;
import com.decafmango.lab_1.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public Role getRoleByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Username %s not found", username)
                ));
        return user.getRole();
    }

    public UserDto whoami() {
        User currentUser = jwtService.currentUser();
        return UserDto.builder()
                .id(currentUser.getId())
                .username(currentUser.getUsername())
                .role(currentUser.getRole())
                .build();
    }

}

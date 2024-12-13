package com.decafmango.lab_1.user.contoller;

import com.decafmango.lab_1.user.dto.AuthResponseDto;
import com.decafmango.lab_1.user.dto.LoginUserDto;
import com.decafmango.lab_1.user.dto.RegisterUserDto;
import com.decafmango.lab_1.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponseDto register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        return authService.register(registerUserDto);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody @Valid LoginUserDto loginUserDto) {
        return authService.login(loginUserDto);
    }

}

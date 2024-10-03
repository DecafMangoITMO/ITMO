package com.decafmango.lab_1.user.contoller;

import com.decafmango.lab_1.user.model.Role;
import com.decafmango.lab_1.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/role/{username}")
    public Role getRoleByUsername(@PathVariable String username) {
        return userService.getRoleByUsername(username);
    }
}

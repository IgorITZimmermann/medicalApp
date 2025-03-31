package de.medical.app.controller;

import de.medical.app.dto.UserDto;
import de.medical.app.model.User;
import de.medical.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping ("/register")
    public ResponseEntity<String> register(@RequestBody UserDto dto ) {
        User newUser = userService.registerUser(dto.getUsername(),dto.getPassword(), dto.getName(), dto.getBirthDate());
        return ResponseEntity.ok("User registered successfully " + newUser.getUsername());
    }

}

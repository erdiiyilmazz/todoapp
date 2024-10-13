package com.erdi.todoapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.erdi.todoapp.dto.request.LoginRequestDto;
import com.erdi.todoapp.dto.request.RegisterRequestDto;
import com.erdi.todoapp.dto.response.AuthResponseDto;
import com.erdi.todoapp.model.entity.User;
import com.erdi.todoapp.service.UserService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User API for authentication")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(description = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        User user = userService.register(registerRequestDto);
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(registerRequestDto.getPassword());
        String token = userService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDto(token));
    }

    @Operation(description = "Login user")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(new AuthResponseDto(userService.login(loginRequestDto)));
    }
}

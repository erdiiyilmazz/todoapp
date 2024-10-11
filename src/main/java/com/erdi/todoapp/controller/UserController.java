package com.erdi.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        User user = userService.register(registerRequestDto);
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(registerRequestDto.getPassword());
        String token = userService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDto(token));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(new AuthResponseDto(userService.login(loginRequestDto)));
    }
}

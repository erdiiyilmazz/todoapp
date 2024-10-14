package com.erdi.todoapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import com.erdi.todoapp.service.UserService;
import com.erdi.todoapp.service.CustomUserDetailsService;
import com.erdi.todoapp.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.erdi.todoapp.dto.request.RegisterRequestDto;
import com.erdi.todoapp.dto.request.LoginRequestDto;
import com.erdi.todoapp.exception.InvalidCredentialsException;
import com.erdi.todoapp.exception.PasswordMatchException;
import com.erdi.todoapp.exception.UserAlreadyExistsException;
import com.erdi.todoapp.model.entity.User;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setup() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("testUser");
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));
    }

    @Test
    public void testRegister() throws Exception {
        RegisterRequestDto registerRequest = new RegisterRequestDto("erdi", "password", "password", "erdi@example.com");
        User createdUser = User.builder()
            .id("123")
            .username("erdi")
            .password("password")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .enabled(true)
            .build();
        
        when(userService.register(any(RegisterRequestDto.class))).thenReturn(createdUser);
        when(userService.login(any(LoginRequestDto.class))).thenReturn("dummy.jwt.token");

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("dummy.jwt.token"));
    }

    @Test
    public void testLogin() throws Exception {
        LoginRequestDto loginRequest = LoginRequestDto.builder()
            .username("erdi")
            .password("password")
            .build();
        String jwtToken = "dummy.jwt.token";
        
        when(userService.login(any(LoginRequestDto.class))).thenReturn(jwtToken);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwtToken));
    }

    @Test
    public void testLoginWithWrongCredentials() throws Exception {
        LoginRequestDto loginRequest = LoginRequestDto.builder()
            .username("erdi")
            .password("wrongpassword")
            .build();
        
        when(userService.login(any(LoginRequestDto.class)))
            .thenThrow(new InvalidCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    public void testRegisterExistingUser() throws Exception {
        RegisterRequestDto registerRequest = new RegisterRequestDto("existingUser", "password", "password", "existing@example.com");
        
        when(userService.register(any(RegisterRequestDto.class)))
            .thenThrow(new UserAlreadyExistsException("User already exists"));

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User already exists"));
    }

    @Test
    public void testRegisterWithMismatchedPasswords() throws Exception {
        RegisterRequestDto registerRequest = new RegisterRequestDto("erdi", "password123", "password", "erdi@example.com");

        when(userService.register(any(RegisterRequestDto.class)))
            .thenThrow(new PasswordMatchException("Passwords do not match"));

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Passwords do not match"));
    }

    @Test
    public void testLoginWithNonExistentUsername() throws Exception {
        LoginRequestDto loginRequest = LoginRequestDto.builder()
            .username("nonexistentUser")
            .password("password")
            .build();        
        when(userService.login(any(LoginRequestDto.class)))
            .thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User not found"));
    }
}

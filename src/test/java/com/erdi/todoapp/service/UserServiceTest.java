package com.erdi.todoapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.erdi.todoapp.dto.request.LoginRequestDto;
import com.erdi.todoapp.dto.request.RegisterRequestDto;
import com.erdi.todoapp.exception.InvalidCredentialsException;
import com.erdi.todoapp.exception.PasswordMatchException;
import com.erdi.todoapp.exception.UserAlreadyExistsException;
import com.erdi.todoapp.model.entity.User;
import com.erdi.todoapp.repository.UserRepository;
import com.erdi.todoapp.security.JwtTokenProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUser() {
        String userId = "123";
        User mockUser = User.builder()
            .id("123")
            .password(passwordEncoder.encode("password"))
            .username("Test User")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .enabled(true)
            .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = userService.getUser("123");

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Test User", result.getUsername());
    }

    @Test
    public void testLoginWithNonExistentUsername() {
        LoginRequestDto loginRequest = LoginRequestDto.builder()
            .username("nonexistentUser")
            .password("password")
            .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, () -> userService.login(loginRequest));
    }

    @Test
    public void testLoginWithWrongCredentials() {
        LoginRequestDto loginRequest = LoginRequestDto.builder()
            .username("wrongusername")
            .password("wrongpassword")
            .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(InvalidCredentialsException.class, () -> userService.login(loginRequest));
    }

    @Test
    public void testRegisterExistingUser() {
        RegisterRequestDto registerRequest = new RegisterRequestDto("erdi", "erdi@example.com", "password", "password");

        when(userRepository.findByUsername("erdi")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(registerRequest));
    }

    @Test
    public void testRegisterWithMismatchedPasswords() {
        RegisterRequestDto registerRequest = new RegisterRequestDto("erdi", "erdi@example.com", "password1", "password2");

        assertThrows(PasswordMatchException.class, () -> userService.register(registerRequest));
    }
}

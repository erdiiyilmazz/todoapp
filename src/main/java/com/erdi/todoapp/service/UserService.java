package com.erdi.todoapp.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.erdi.todoapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.erdi.todoapp.dto.request.LoginRequestDto;
import com.erdi.todoapp.dto.request.RegisterRequestDto;
import com.erdi.todoapp.exception.InvalidCredentialsException;
import com.erdi.todoapp.exception.PasswordMatchException;
import com.erdi.todoapp.exception.UserAlreadyExistsException;
import com.erdi.todoapp.model.entity.User;
import com.erdi.todoapp.security.JwtTokenProvider;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public User register(RegisterRequestDto registerRequestDto) throws UserAlreadyExistsException, PasswordMatchException {
        if (userRepository.findByUsername(registerRequestDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with this username already exists");
        }
        if (!registerRequestDto.getPassword().equals(registerRequestDto.getPasswordRepeat())) {
            throw new PasswordMatchException("Passwords do not match");
        }

        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public String login(LoginRequestDto loginRequestDto) throws AuthenticationException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(),
                    loginRequestDto.getPassword()
                )
            );
            return jwtTokenProvider.generateToken(authentication);
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }
}

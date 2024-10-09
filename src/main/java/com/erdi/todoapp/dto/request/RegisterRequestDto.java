package com.erdi.todoapp.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDto{
        @NotBlank 
        private String username;

        @NotBlank 
        private String password;

        @NotBlank 
        private String passwordRepeat;
}
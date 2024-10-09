package com.erdi.todoapp.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private LocalDateTime createdAt;
}

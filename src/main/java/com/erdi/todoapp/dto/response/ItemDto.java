package com.erdi.todoapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private String id;
    @NotBlank(message = "Title is required")
    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;
}

package com.erdi.todoapp.dto.response;

import lombok.Data;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;

@Data
public class ItemDto {
    private String id;
    @NotBlank(message = "Title is required")
    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;
}

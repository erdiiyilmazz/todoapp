package com.erdi.todoapp.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateItemDto {
    @JsonProperty("title")
    @NotBlank(message = "Title is required")
    private String title;

    @JsonProperty("content")
    private String content;
}

package com.erdi.todoapp.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreateItemDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;
}

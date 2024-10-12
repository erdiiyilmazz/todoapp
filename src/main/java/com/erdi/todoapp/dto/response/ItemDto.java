package com.erdi.todoapp.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class ItemDto {
    private String id;
    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;
}

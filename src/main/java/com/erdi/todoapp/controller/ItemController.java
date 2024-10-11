package com.erdi.todoapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.erdi.todoapp.dto.request.CreateItemDto;
import com.erdi.todoapp.dto.response.ItemDto;
import com.erdi.todoapp.model.entity.User;
import com.erdi.todoapp.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody CreateItemDto createItemDto, User user) {
        ItemDto createdItem = itemService.createItem(createItemDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

}

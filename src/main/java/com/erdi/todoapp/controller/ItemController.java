package com.erdi.todoapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.erdi.todoapp.dto.request.CreateItemDto;
import com.erdi.todoapp.dto.response.ItemDto;
import com.erdi.todoapp.model.entity.User;
import com.erdi.todoapp.service.ItemService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody CreateItemDto createItemDto, 
                                              @AuthenticationPrincipal User user) {
        ItemDto createdItem = itemService.createItem(createItemDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems(@AuthenticationPrincipal User user) {
        List<ItemDto> items = itemService.getAllItems(user);
        return ResponseEntity.ok(items);
    }
}

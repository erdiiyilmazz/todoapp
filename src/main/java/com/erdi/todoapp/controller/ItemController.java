package com.erdi.todoapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.erdi.todoapp.dto.request.CreateItemDto;
import com.erdi.todoapp.dto.request.UpdateItemDto;
import com.erdi.todoapp.dto.response.ItemDto;
import com.erdi.todoapp.model.entity.User;
import com.erdi.todoapp.service.ItemService;
import jakarta.validation.Valid;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/items")
@Tag(name = "Items", description = "Todo Item API")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    
    @Operation(description = "Create a new todo item")
    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody CreateItemDto createItemDto, 
                                              @AuthenticationPrincipal User user) {
        ItemDto createdItem = itemService.createItem(createItemDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @Operation(description = "Get all todo items")
    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems(@AuthenticationPrincipal User user) {
        List<ItemDto> items = itemService.getAllItems(user);
        return ResponseEntity.ok(items);
    }

    @Operation(description = "Get a todo item by id")
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItem(@PathVariable String itemId, @AuthenticationPrincipal User user) {
        ItemDto item = itemService.getItem(itemId, user);
        return ResponseEntity.ok(item);
    }

    @Operation(description = "Update a todo item")
    @PutMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable String itemId, 
                                              @Valid @RequestBody UpdateItemDto updateItemDto, 
                                              @AuthenticationPrincipal User user) {
        ItemDto updatedItem = itemService.updateItem(itemId, updateItemDto, user);
        return ResponseEntity.ok(updatedItem);
    }

    @Operation(description = "Delete a todo item")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable String itemId, @AuthenticationPrincipal User user) {
        itemService.deleteItem(itemId, user);
        return ResponseEntity.noContent().build();
    }
}

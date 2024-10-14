package com.erdi.todoapp.controller;

import com.erdi.todoapp.dto.request.CreateItemDto;
import com.erdi.todoapp.dto.request.UpdateItemDto;
import com.erdi.todoapp.dto.response.ItemDto;
import com.erdi.todoapp.model.entity.User;
import com.erdi.todoapp.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId("user1");
        mockUser.setUsername("testuser");
    }

    @Test
    void createItem_ShouldReturnCreatedItem() {
        CreateItemDto createItemDto = new CreateItemDto();
        createItemDto.setTitle("Test Todo Item");
        createItemDto.setContent("Test Content");

        ItemDto createdItemDto = ItemDto.builder()
            .id("item1")
            .title("Test Todo Item")
            .content("Test Content")
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();

        when(itemService.createItem(any(CreateItemDto.class), any(User.class))).thenReturn(createdItemDto);

        ResponseEntity<ItemDto> response = itemController.createItem(createItemDto, mockUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdItemDto, response.getBody());
        verify(itemService).createItem(createItemDto, mockUser);
    }

    @Test
    void getAllItems_ShouldReturnListOfItems() {
        List<ItemDto> items = Arrays.asList(
            ItemDto.builder()
                .id("item1")
                .title("Item 1")
                .content("Content 1")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build(),
            ItemDto.builder()
                .id("item2")
                .title("Item 2")
                .content("Content 2")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build()
        );

        when(itemService.getAllItems(any(User.class))).thenReturn(items);

        ResponseEntity<List<ItemDto>> response = itemController.getAllItems(mockUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(items, response.getBody());
        verify(itemService).getAllItems(mockUser);
    }

    @Test
    void getItem_ShouldReturnItem() {
        String itemId = "item1";
        ItemDto itemDto = ItemDto.builder()
            .id(itemId)
            .title("Test Todo Item")
            .content("Test Content")
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();

        when(itemService.getItem(itemId, mockUser)).thenReturn(itemDto);

        ResponseEntity<ItemDto> response = itemController.getItem(itemId, mockUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemDto, response.getBody());
        verify(itemService).getItem(itemId, mockUser);
    }

    @Test
    void updateItem_ShouldReturnUpdatedItem() {
        String itemId = "item1";
        UpdateItemDto updateItemDto = new UpdateItemDto();
        updateItemDto.setTitle("Updated Item");
        updateItemDto.setContent("Updated Content");

        ItemDto updatedItemDto = ItemDto.builder()
            .id(itemId)
            .title("Updated Item")
            .content("Updated Content")
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();

        when(itemService.updateItem(eq(itemId), any(UpdateItemDto.class), any(User.class))).thenReturn(updatedItemDto);

        ResponseEntity<ItemDto> response = itemController.updateItem(itemId, updateItemDto, mockUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedItemDto, response.getBody());
        verify(itemService).updateItem(itemId, updateItemDto, mockUser);
    }

    @Test
    void deleteItem_ShouldReturnNoContent() {
        String itemId = "item1";

        ResponseEntity<Void> response = itemController.deleteItem(itemId, mockUser);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(itemService).deleteItem(itemId, mockUser);
    }
}

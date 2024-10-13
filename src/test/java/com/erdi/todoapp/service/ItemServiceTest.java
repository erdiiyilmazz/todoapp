package com.erdi.todoapp.service;

import com.erdi.todoapp.dto.request.CreateItemDto;
import com.erdi.todoapp.dto.request.UpdateItemDto;
import com.erdi.todoapp.dto.response.ItemDto;
import com.erdi.todoapp.model.entity.Item;
import com.erdi.todoapp.model.entity.User;
import com.erdi.todoapp.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId("user1");
        mockUser.setUsername("testuser");
    }

    @Test
    void createItem_ShouldReturnCreatedItemDto() {
        CreateItemDto createItemDto = new CreateItemDto();
        createItemDto.setTitle("Test Todo Item");
        createItemDto.setContent("Test Content");

        Item savedItem = new Item();
        savedItem.setId("item1");
        savedItem.setTitle("Test Todo Item");
        savedItem.setContent("Test Content");
        savedItem.setUserId(mockUser.getUsername());
        savedItem.setCreateDate(new Date());
        savedItem.setUpdateDate(new Date());

        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        ItemDto result = itemService.createItem(createItemDto, mockUser);

        assertNotNull(result);
        assertEquals("item1", result.getId());
        assertEquals("Test Todo Item", result.getTitle());
        assertEquals("Test Content", result.getContent());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void getAllItems_ShouldReturnListOfItemDtos() {
        List<Item> items = List.of(
            Item.builder()
                .title("Item 1")
                .content("Content 1")
                .userId(mockUser.getUsername())
                .createDate(new Date())
                .updateDate(new Date())
                .build(),
            Item.builder()
                .title("Item 2")
                .content("Content 2")
                .userId(mockUser.getUsername())
                .userId(mockUser.getUsername())
                .createDate(new Date())
                .updateDate(new Date())
                .build()
        );
        when(itemRepository.findByUserIdAndDeletedFalse(mockUser.getUsername())).thenReturn(items);

        List<ItemDto> result = itemService.getAllItems(mockUser);

        assertEquals(2, result.size());
        verify(itemRepository).findByUserIdAndDeletedFalse(mockUser.getUsername());
    }

    @Test
    void getItem_ShouldReturnItemDto() {
        String itemId = "item1";
        Item item = new Item();
        item.setId(itemId);
        item.setTitle("Test Todo Item");
        item.setContent("Test Content");
        item.setUserId(mockUser.getUsername());
        item.setCreateDate(new Date());
        item.setUpdateDate(new Date());

        when(itemRepository.findByIdAndUserIdAndDeletedFalse(itemId, mockUser.getUsername())).thenReturn(Optional.of(item));

        ItemDto result = itemService.getItem(itemId, mockUser);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals("Test Todo Item", result.getTitle());
        assertEquals("Test Content", result.getContent());
        verify(itemRepository).findByIdAndUserIdAndDeletedFalse(itemId, mockUser.getUsername());
    }

    @Test
    void updateItem_ShouldReturnUpdatedItemDto() {
        String itemId = "item1";
        UpdateItemDto updateItemDto = new UpdateItemDto();
        updateItemDto.setTitle("Updated Item");
        updateItemDto.setContent("Updated Content");

        Item existingItem = new Item();
        existingItem.setId(itemId);
        existingItem.setTitle("Test Todo Item");
        existingItem.setContent("Test Content");
        existingItem.setUserId(mockUser.getUsername());
        existingItem.setCreateDate(new Date());
        existingItem.setUpdateDate(new Date());

        Item updatedItem = new Item();
        updatedItem.setId(itemId);
        updatedItem.setTitle("Updated Item");
        updatedItem.setContent("Updated Content");
        updatedItem.setUserId(mockUser.getUsername());
        updatedItem.setCreateDate(existingItem.getCreateDate());
        updatedItem.setUpdateDate(new Date());

        when(itemRepository.findByIdAndUserIdAndDeletedFalse(itemId, mockUser.getUsername())).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);

        ItemDto result = itemService.updateItem(itemId, updateItemDto, mockUser);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals("Updated Item", result.getTitle());
        assertEquals("Updated Content", result.getContent());
        verify(itemRepository).findByIdAndUserIdAndDeletedFalse(itemId, mockUser.getUsername());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void deleteItem_ShouldMarkItemAsDeleted() {
                String itemId = "item1";
        Item item = new Item();
        item.setId(itemId);
        item.setTitle("Test Todo Item");
        item.setContent("Test Content");
        item.setUserId(mockUser.getUsername());
        item.setCreateDate(new Date());
        item.setUpdateDate(new Date());

        when(itemRepository.findByIdAndUserIdAndDeletedFalse(itemId, mockUser.getUsername())).thenReturn(Optional.of(item));
        itemService.deleteItem(itemId, mockUser);

        verify(itemRepository).findByIdAndUserIdAndDeletedFalse(itemId, mockUser.getUsername());
        verify(itemRepository).save(argThat(savedItem -> savedItem.isDeleted() && savedItem.getUpdateDate() != null));
    }

    @Test
    void getItem_ShouldThrowResponseStatusException_WhenItemNotFound() {
        String itemId = "nonexistent";
        when(itemRepository.findByIdAndUserIdAndDeletedFalse(itemId, mockUser.getUsername())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> itemService.getItem(itemId, mockUser));
    }
}

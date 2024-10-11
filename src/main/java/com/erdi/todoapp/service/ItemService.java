package com.erdi.todoapp.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.erdi.todoapp.dto.request.CreateItemDto;
import com.erdi.todoapp.dto.response.ItemDto;
import com.erdi.todoapp.model.entity.Item;
import com.erdi.todoapp.model.entity.User;
import com.erdi.todoapp.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemDto createItem(CreateItemDto createItemDto, User user) {
        Item item = new Item();
        item.setUserId(user.getUsername());
        item.setTitle(createItemDto.getTitle()); 
        item.setContent(createItemDto.getContent()); 
        item.setDeleted(false);
        item.setCreateDate(new Date());
        item.setUpdateDate(new Date());
        
        Item savedItem = itemRepository.save(item);
        return convertToDto(savedItem);
    }

    private ItemDto convertToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setTitle(item.getTitle());
        itemDto.setContent(item.getContent());
        itemDto.setCreatedAt(item.getCreateDate());
        itemDto.setUpdatedAt(item.getUpdateDate());
        return itemDto;
    }

}

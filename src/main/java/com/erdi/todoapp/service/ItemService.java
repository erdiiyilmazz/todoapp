package com.erdi.todoapp.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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

    public List<ItemDto> getAllItems(User user) {
        List<Item> items = itemRepository.findByUserIdAndDeletedFalse(user.getUsername());
        return items.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
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

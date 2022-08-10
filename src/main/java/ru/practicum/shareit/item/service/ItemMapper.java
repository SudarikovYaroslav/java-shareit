package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Service
public class ItemMapper {
    public ItemDto convertItemIntoItemDto(Item item) {
        return new ItemDto(item.getName(), item.getDescription());
    }
}

package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemMapper {
    public ItemDto mapItemToItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public Item mapItemDtoToItem(ItemDto itemDto, Long ownerId) {
        return new Item(null, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), ownerId);
    }

    public List<ItemDto> mapItemListToItemDtoList(List<Item> userItems) {
        if (userItems.isEmpty()) {
            return new ArrayList<>();
        }

        List<ItemDto> result = new ArrayList<>();
        for (Item item : userItems) {
            ItemDto itemDto = mapItemToItemDto(item);
            result.add(itemDto);
        }
        return result;
    }
}

package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto dto, Long userId);

    ItemDto updateItem(ItemDto dto, Long itemId, Long userId);

    ItemDto findItemById(Long itemId);

    List<ItemDto> findAllItems(Long userId);

    List<ItemDto> findItemsByRequest(String text);
}

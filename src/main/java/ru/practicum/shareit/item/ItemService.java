package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    Item createItem(Item item);

    Item updateItem(Item item);

    Item findItemById(Long itemId);

    List<Item> findAllItems(Long userId);

    List<Item> findItemsByRequest(String text);
}

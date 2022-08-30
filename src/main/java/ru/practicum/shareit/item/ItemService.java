package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDetailedDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto dto, Long userId);

    CommentDetailedDto createComment(CommentCreateDto dto, Long itemId, Long userId);

    ItemDto updateItem(ItemDto dto, Long itemId, Long userId);

    ItemDto findItemById(Long itemId, Long userId);

    List<ItemDto> findAllItems(Long userId);

    List<ItemDto> findItemsByRequest(String text, Long userId);
}

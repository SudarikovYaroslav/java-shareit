package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * // TODO .
 */
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader Long userId) {
        Item item = itemMapper.convertItemDtoIntoItem(itemDto, userId);
        return itemMapper.convertItemIntoItemDto(itemService.createItem(item));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable long itemId,
                              @RequestHeader long userId) {
        Item item = itemMapper.convertItemDtoIntoItem(itemDto, userId);
        item.setId(itemId);
        return itemMapper.convertItemIntoItemDto(itemService.updateItem(item));
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@PathVariable long itemId) {
        return itemMapper.convertItemIntoItemDto(itemService.findItemById(itemId));
    }

    @GetMapping
    public List<ItemDto> findAllItems(@RequestHeader long userId) {
        List<Item> userItems = itemService.findAllItems(userId);
        return itemMapper.convertItemListToItemDtoList(userItems);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsByRequest(@RequestParam String text) {
        List<Item> foundItems = itemService.findItemsByRequest(text);
        return itemMapper.convertItemListToItemDtoList(foundItems);
    }
}

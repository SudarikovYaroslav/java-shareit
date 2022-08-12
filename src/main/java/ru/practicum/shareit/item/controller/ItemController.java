package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper mapper;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = mapper.mapItemDtoToItem(itemDto, userId);
        return mapper.mapItemToItemDto(itemService.createItem(item));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable long itemId,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        Item item = mapper.mapItemDtoToItem(itemDto, userId);
        item.setId(itemId);
        return mapper.mapItemToItemDto(itemService.updateItem(item));
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@PathVariable long itemId) {
        return mapper.mapItemToItemDto(itemService.findItemById(itemId));
    }

    @GetMapping
    public List<ItemDto> findAllItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<Item> userItems = itemService.findAllItems(userId);
        return mapper.mapItemListToItemDtoList(userItems);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsByRequest(@RequestParam String text) {
        List<Item> foundItems = itemService.findItemsByRequest(text);
        return mapper.mapItemListToItemDtoList(foundItems);
    }
}

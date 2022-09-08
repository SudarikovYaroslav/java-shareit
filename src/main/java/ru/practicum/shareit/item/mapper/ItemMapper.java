package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.InRequestItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toDto(Item item, List<Comment> comments) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        if (comments != null) {
            dto.setComments(CommentMapper.toCommentDetailedDtoList(comments));
        }
        return dto;
    }

    public static ItemDto toDto(Item item,
                         Booking lastBooking,
                         Booking nextBooking,
                         List<Comment> comments) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setLastBooking(BookingMapper.bookingInItemDto(lastBooking));
        dto.setNextBooking(BookingMapper.bookingInItemDto(nextBooking));
        if (comments != null) {
            dto.setComments(CommentMapper.toCommentDetailedDtoList(comments));
        }
        return dto;
    }

    public static Item toModel(ItemDto itemDto, Long ownerId) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(ownerId);
        item.setRequestId(itemDto.getRequestId());
        return item;
    }

    public static InRequestItemDto toInRequestItemDto(Item item) {
        InRequestItemDto dto = new InRequestItemDto();
        dto.setId(item.getOwner());
        dto.setName(item.getName());
        dto.setOwner(item.getOwner());
        return dto;
    }

    public static List<InRequestItemDto> toInRequestItemDtoList(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toInRequestItemDto)
                .collect(Collectors.toList());
    }
}
package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingInItemDto;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
public class ItemMapper {
    public ItemDto toDto(Item item, List<Comment> comments) {
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

    public ItemDto toDto(Item item,
                         BookingInItemDto lastBooking,
                         BookingInItemDto nextBooking,
                         List<Comment> comments) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setLastBooking(lastBooking);
        dto.setNextBooking(nextBooking);
        if (comments != null) {
            dto.setComments(CommentMapper.toCommentDetailedDtoList(comments));
        }
        return dto;
    }

    public Item toModel(ItemDto itemDto, Long ownerId) {
        return new Item(null, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), ownerId);
    }
}
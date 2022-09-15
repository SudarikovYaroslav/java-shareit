package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemMapperTest {

    public static final long ID = 1L;
    public static final LocalDateTime CREATED_DATE = LocalDateTime.now();

    private Item item;
    private User user;
    private ItemDto itemDto;
    private Comment comment;
    private Booking booking;

    @BeforeEach
    public void beforeEach() {
        item = new Item(
                ID,
                "name",
                "description",
                true,
                ID,
                ID + 1);

        itemDto = new ItemDto(
                ID,
                "name",
                "description",
                true,
                null,
                null,
                null,
                ID + 1);

        user = new User(ID, "name", "user@emali.com");
        comment = new Comment(ID, "comment", item, user, CREATED_DATE);

        booking = new Booking(ID,
                CREATED_DATE,
                CREATED_DATE.plusDays(7),
                item,
                user,
                BookingStatus.APPROVED);
    }

    // сразу 2
    @Test
    public void toDto() {
        ItemDto resultWithoutBookings = ItemMapper
                .toDto(item, Collections.singletonList(comment));
        ItemDto resultWithBookings = ItemMapper
                .toDto(item, null, null, Collections.singletonList(comment));

        assertNotNull(resultWithoutBookings);
        assertNotNull(resultWithBookings);
        assertEquals(item.getId(), resultWithBookings.getId());
        assertEquals(item.getId(), resultWithoutBookings.getId());
        assertFalse(resultWithBookings.getComments().isEmpty());
        assertFalse(resultWithBookings.getComments().isEmpty());
    }

    @Test
    public void toModel() {
        Item result = ItemMapper.toModel(itemDto, ID);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(ID, result.getOwner());
    }

    @Test
    public void toRequestItemDto() {
        ItemInRequestDto result = ItemMapper.toRequestItemDto(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
    }

    @Test
    public void toRequestItemDtoList() {
        List<ItemInRequestDto> result = ItemMapper
                .toRequestItemDtoList(Collections.singletonList(item));

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(item.getId(), result.get(0).getId());
        assertEquals(item.getName(), result.get(0).getName());
        assertEquals(item.getDescription(), result.get(0).getDescription());
    }
}

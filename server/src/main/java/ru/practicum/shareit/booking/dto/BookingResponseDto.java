package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Getter
@Setter
@NoArgsConstructor
public class BookingResponseDto {
    private Long id;
    private BookingStatus status;
    private User booker;
    private Item item;
    private String name;
}
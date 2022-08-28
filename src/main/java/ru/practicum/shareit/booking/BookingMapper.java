package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDetailedDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingPostResponseDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Service
public class BookingMapper {
    public Booking toModel(BookingPostDto dto, Item item, User user) {
        Booking booking = new Booking();
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    public BookingPostResponseDto toPostDto(Booking booking, Item item) {
        BookingPostResponseDto dto = new BookingPostResponseDto();
        dto.setId(booking.getId());
        dto.setItem(item);
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        return dto;
    }

    public BookingResponseDto toResponseDto(Booking booking, User booker, Item item) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setStatus(booking.getStatus());
        dto.setBooker(booker);
        dto.setItem(item);
        dto.setName(item.getName());
        return dto;
    }

    public BookingDetailedDto toDetailedDto(Booking booking, User booker, Item item) {
        BookingDetailedDto dto = new BookingDetailedDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        dto.setBooker(booker);
        dto.setItem(item);
        dto.setName(item.getName());
        return dto;
    }
}
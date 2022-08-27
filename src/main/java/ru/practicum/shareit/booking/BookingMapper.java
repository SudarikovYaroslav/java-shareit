package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;

@Service
public class BookingMapper {
    public Booking toModel(BookingPostDto bookingPostDto, Long userId) {
        Booking booking = new Booking();
        booking.setStart(bookingPostDto.getStart());
        booking.setEnd(bookingPostDto.getEnd());
        booking.setItem(bookingPostDto.getItemId());
        booking.setBooker(userId);
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    public BookingPostDto toPostDto(Booking booking) {
        BookingPostDto dto = new BookingPostDto();
        dto.setId(booking.getId());
        dto.setItemId(booking.getItem());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        return dto;
    }

    public BookingResponseDto toResponseDto(Booking booking, String itemName) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setStatus(booking.getStatus());
        dto.setBooker(booking.getBooker());
        dto.setItem(booking.getItem());
        dto.setName(itemName);
        return dto;
    }
}

package ru.practicum.shareit.booking;

public interface BookingService {

    BookingPostDto createBooking(BookingPostDto dto, Long userId);
}

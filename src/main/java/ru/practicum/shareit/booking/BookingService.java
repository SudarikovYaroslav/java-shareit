package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDetailedDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingPostResponseDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

public interface BookingService {

    BookingPostResponseDto createBooking(BookingPostDto dto, Long userId);

    BookingResponseDto patchBooking(Long bookingId, Boolean approved, Long userId);

    BookingDetailedDto findById(Long bookingId, Long userId);
}

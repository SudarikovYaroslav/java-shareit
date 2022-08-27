package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation_markers.Create;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private BookingService bookingService;

    @PostMapping
    public BookingPostDto createBooking(@RequestBody @Validated(Create.class) BookingPostDto dto,
                                        @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.createBooking(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingPostDto patchBooking(@RequestBody BookingPostDto bookingPostDto,
                                       @PathVariable Long bookingId,
                                       @RequestParam Boolean approved) {
        return null;
    }

    @GetMapping("/{bookingId}")
    public BookingPostDto findById(@PathVariable Long bookingId) {
        return null;
    }

    // Получение списка всех бронирований ТЕКУЩЕГО ПОЛЬЗОВАТЕЛЯ
    @GetMapping
    public List<BookingPostDto> findAllBookings(@RequestParam BookingStatus state) {
        return null;
    }

    //Получение списка бронирований для всех вещей текущего пользователя.
    @GetMapping("/owner")
    public List<BookingPostDto> findAll(@RequestParam BookingStatus status) {
        return null;
    }
}

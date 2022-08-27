package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UnavailableBookingException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    public static final String BOOKING_INVALID_MESSAGE = "недопустимые значения времени бронирования: ";
    public static final String UNAVAILABLE_BOOKING_MESSAGE = "в данный момент невозможно забронировать item: ";

    private final BookingMapper mapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingPostDto createBooking(BookingPostDto dto, Long userId) {
        Booking booking = mapper.toModel(dto, userId);

        userRepository.findById(userId).orElseThrow();
        Item item = itemRepository.findById(booking.getItem()).orElseThrow();
        if (!item.getAvailable()) {
            throw new UnavailableBookingException(UNAVAILABLE_BOOKING_MESSAGE + booking.getId());
        }

        if (!isStartBeforeEnd(booking)) {
            throw new IllegalArgumentException(BOOKING_INVALID_MESSAGE +
                    "start: " + booking.getStart() + " end: " + booking.getEnd() + " now: ");
        }
        return mapper.toPostDto(bookingRepository.save(booking));
    }

    private boolean isStartBeforeEnd(Booking booking) {
        return booking.getStart().isBefore(booking.getEnd());
    }
}
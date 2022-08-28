package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDetailedDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingPostResponseDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exceptions.DeniedAccessException;
import ru.practicum.shareit.exceptions.UnavailableBookingException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    public static final String BOOKING_INVALID_MESSAGE = "недопустимые значения времени бронирования: ";
    public static final String UNAVAILABLE_BOOKING_MESSAGE = "в данный момент невозможно забронировать item: ";
    public static final String DENIED_ACCESS_MESSAGE = "Пользователь не является владельцем вещи или брони userId: ";
    public static final String DENIED_PATCH_ACCESS_MESSAGE = "Пользователь не является владельцем вещи userId: ";

    private final BookingMapper mapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingPostResponseDto createBooking(BookingPostDto dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Item item = itemRepository.findById(dto.getItemId()).orElseThrow();

        if (!item.getAvailable()) {
            throw new UnavailableBookingException(UNAVAILABLE_BOOKING_MESSAGE + item.getId());
        }

        Booking booking = mapper.toModel(dto, item, user);

        if (!isStartBeforeEnd(booking)) {
            throw new IllegalArgumentException(BOOKING_INVALID_MESSAGE +
                    "start: " + booking.getStart() + " end: " + booking.getEnd() + " now: ");
        }
        return mapper.toPostDto(bookingRepository.save(booking), item);
    }

    @Override
    public BookingResponseDto patchBooking(Long bookingId, Boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow();

        if (!item.getOwner().equals(userId)) {
            throw new DeniedAccessException(DENIED_PATCH_ACCESS_MESSAGE + userId + " itemId: " + item.getId());
        }
        booking.setStatus(detectStatus(approved));
        return mapper.toResponseDto(bookingRepository.save(booking), booking.getBooker(), item);
    }

    @Override
    public BookingDetailedDto findById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        if (!isAuthorOrOwner(booking, item, userId)) {
            throw new DeniedAccessException(DENIED_ACCESS_MESSAGE + userId);
        }
        return mapper.toDetailedDto(booking, user, item);
    }

    private boolean isStartBeforeEnd(Booking booking) {
        return booking.getStart().isBefore(booking.getEnd());
    }

    private boolean isAuthorOrOwner(Booking booking, Item bookingItem, Long userId) {
        return booking.getBooker().equals(userId) || bookingItem.getOwner().equals(userId);
    }

    private BookingStatus detectStatus(Boolean b) {
        return b ? BookingStatus.APPROVED : BookingStatus.REJECTED;
    }
}
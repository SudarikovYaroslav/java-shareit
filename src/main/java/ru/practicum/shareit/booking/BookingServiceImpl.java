package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    public static final String ALL = "ALL";
    public static final String PAST = "PAST";
    public static final String FUTURE = "FUTURE";
    public static final String WAITING = "WAITING";
    public static final String CURRENT = "CURRENT";
    public static final String REJECTED = "REJECTED";
    public static final String BOOKING_INVALID_MESSAGE = "недопустимые значения времени бронирования: ";
    public static final String UNAVAILABLE_BOOKING_MESSAGE = "в данный момент невозможно забронировать item: ";
    public static final String DENIED_PATCH_ACCESS_MESSAGE = "Пользователь не является владельцем вещи userId: ";
    public static final String DENIED_ACCESS_MESSAGE = "Пользователь не является владельцем вещи или брони userId: ";

    private final BookingMapper mapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingPostResponseDto createBooking(BookingPostDto dto, Long userId) {
        if (!isStartBeforeEnd(dto)) {
            throw new IllegalArgumentException(BOOKING_INVALID_MESSAGE +
                    "start: " +dto.getStart() + " end: " + dto.getEnd() + " now: ");
        }

        User user = userRepository.findById(userId).orElseThrow();
        Item item = itemRepository.findById(dto.getItemId()).orElseThrow();

        if (!item.getAvailable()) {
            throw new UnavailableBookingException(UNAVAILABLE_BOOKING_MESSAGE + item.getId());
        }

        Booking booking = mapper.toModel(dto, item, user);
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
        userRepository.findById(userId).orElseThrow();
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        Long itemOwner = booking.getItem().getOwner();
        Long bookingOwner = booking.getBooker().getId();
        boolean itemOrBookingOwner = userId.equals(bookingOwner) || userId.equals(itemOwner);

        if (!itemOrBookingOwner) {
            throw new DeniedAccessException(DENIED_ACCESS_MESSAGE + userId);
        }
        return mapper.toDetailedDto(booking);
    }

    // TODO Получение списка всех бронирований текущего пользователя.
    //  Эндпоинт — GET /bookings?state={state}. Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
    //  Также он может принимать значения CURRENT (англ. «текущие»), **PAST** (англ. «завершённые»),
    //  FUTURE (англ. «будущие»), WAITING (англ. «ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
    //  Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
    @Override
    public List<BookingDetailedDto> findAllBookings(State state, Long userId) {
        userRepository.findById(userId).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();
        Sort sort = Sort.by("start").descending();

        switch (state.name()) {
            case (REJECTED) -> bookings = bookingRepository
                    .findByBooker_IdAndStatus(userId, BookingStatus.REJECTED, sort);
            case (WAITING) -> bookings = bookingRepository
                    .findByBooker_IdAndStatus(userId, BookingStatus.WAITING, sort);
            case (CURRENT) -> bookings = bookingRepository
                    .findByBooker_IdAndStatus(userId, BookingStatus.APPROVED, sort);
            case (FUTURE) -> bookings = bookingRepository
                        .findByBooker_IdAndStartIsAfter(userId, now, sort);
            case (PAST) -> bookings = bookingRepository
                        .findByBooker_IdAndEndIsBefore(userId, now, sort);
            case (ALL) -> bookings = bookingRepository.findByBooker_Id(userId, sort);
        }
        return mapper.toListDetailedDto(bookings);
    }

    private boolean isStartBeforeEnd(BookingPostDto dto) {
        return dto.getStart().isBefore(dto.getEnd());
    }

    private BookingStatus detectStatus(Boolean b) {
        return b ? BookingStatus.APPROVED : BookingStatus.REJECTED;
    }
}
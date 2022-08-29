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
import ru.practicum.shareit.exceptions.UnsupportedStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    public static final String ALL = "ALL";
    public static final String PAST = "PAST";
    public static final String FUTURE = "FUTURE";
    public static final String WAITING = "WAITING";
    public static final String CURRENT = "CURRENT";
    public static final String REJECTED = "REJECTED";
    public static final String ILLEGAL_SATE_MESSAGE = "  state: ";
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
            throw new NoSuchElementException(DENIED_PATCH_ACCESS_MESSAGE + userId + " itemId: " + item.getId());
        }
        booking.setStatus(detectStatus(approved));
        return mapper.toResponseDto(bookingRepository.save(booking), booking.getBooker(), item);
    }

    @Override
    public BookingDetailedDto findById(Long bookingId, Long userId) {
        checkIfUserExists(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        Long itemOwner = booking.getItem().getOwner();
        Long bookingOwner = booking.getBooker().getId();
        boolean itemOrBookingOwner = userId.equals(bookingOwner) || userId.equals(itemOwner);

        if (!itemOrBookingOwner) {
            throw new NoSuchElementException(DENIED_ACCESS_MESSAGE + userId);
        }
        return mapper.toDetailedDto(booking);
    }

    @Override
    public List<BookingDetailedDto> findAllBookings(String state, Long userId) {
        State status = tryDetectState(state);
        checkIfUserExists(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        Sort sort = Sort.by("start").descending();

        switch (status.name()) {
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

            default -> throw new IllegalArgumentException(ILLEGAL_SATE_MESSAGE);
        }
        return mapper.toListDetailedDto(bookings);
    }

    @Override
    public List<BookingDetailedDto> findAll(String state, Long userId) {
        State status = tryDetectState(state);
        checkIfUserExists(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        Sort sort = Sort.by("start").descending();

        switch (status.name()) {
            case (REJECTED) -> bookings = bookingRepository
                    .findBookingByItem_OwnerAndStatus(userId, BookingStatus.REJECTED, sort);
            case (WAITING) -> bookings = bookingRepository
                    .findBookingByItem_OwnerAndStatus(userId, BookingStatus.WAITING, sort);
            case (CURRENT) -> bookings = bookingRepository
                    .findBookingByItem_OwnerAndStatus(userId, BookingStatus.APPROVED, sort);
            case (FUTURE) -> bookings = bookingRepository
                    .findBookingByItem_OwnerAndStartIsAfter(userId, now, sort);
            case (PAST) -> bookings = bookingRepository
                    .findBookingByItem_OwnerAndEndIsBefore(userId, now, sort);
            case (ALL) -> bookings = bookingRepository
                    .findBookingByItem_Owner(userId, sort);

            default -> throw new IllegalArgumentException(ILLEGAL_SATE_MESSAGE);
        }
        return mapper.toListDetailedDto(bookings);
    }

    private void checkIfUserExists(Long userId) {
        userRepository.findById(userId).orElseThrow();
    }

    private State tryDetectState(String state) {
        State status;
        try {
            status = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException(ILLEGAL_SATE_MESSAGE + state);
        }
        return status;
    }

    private boolean isStartBeforeEnd(BookingPostDto dto) {
        return dto.getStart().isBefore(dto.getEnd());
    }

    private BookingStatus detectStatus(Boolean b) {
        return b ? BookingStatus.APPROVED : BookingStatus.REJECTED;
    }
}
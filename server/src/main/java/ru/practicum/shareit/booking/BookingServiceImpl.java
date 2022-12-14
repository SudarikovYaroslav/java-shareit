package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.exceptions.InvalidBookingException;
import ru.practicum.shareit.exceptions.UnavailableBookingException;
import ru.practicum.shareit.exceptions.UnsupportedStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static ru.practicum.shareit.booking.BookingStatus.REJECTED;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    public static final String ILLEGAL_SATE_MESSAGE = "  state: ";
    public static final Sort SORT = Sort.by("start").descending();
    public static final String INVALID_BUCKING = "нельзя забронировать свою же вещь";
    public static final String SATE_ALREADY_SET_MESSAGE = "статус уже выставлен state: ";
    public static final String UNAVAILABLE_BOOKING_MESSAGE = "в данный момент невозможно забронировать item: ";
    public static final String DENIED_PATCH_ACCESS_MESSAGE = "пользователь не является владельцем вещи userId: ";
    public static final String DENIED_ACCESS_MESSAGE = "пользователь не является владельцем вещи или брони userId: ";

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingPostResponseDto createBooking(BookingPostDto dto, Long userId) {

        User user = userRepository.findById(userId).orElseThrow();
        Item item = itemRepository.findById(dto.getItemId()).orElseThrow();

        if (userId.equals(item.getOwner())) {
            throw new InvalidBookingException(INVALID_BUCKING);
        }

        if (!item.getAvailable()) {
            throw new UnavailableBookingException(UNAVAILABLE_BOOKING_MESSAGE + item.getId());
        }

        Booking booking = BookingMapper.toModel(dto, item, user);
        booking = bookingRepository.save(booking);
        return BookingMapper.toPostResponseDto(booking, item);
    }

    @Override
    @Transactional
    public BookingResponseDto patchBooking(Long bookingId, Boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow();

        if (!item.getOwner().equals(userId)) {
            throw new NoSuchElementException(DENIED_PATCH_ACCESS_MESSAGE + userId + " itemId: " + item.getId());
        }
        BookingStatus status = convertToStatus(approved);

        if (booking.getStatus().equals(status)) {
            throw new IllegalArgumentException(SATE_ALREADY_SET_MESSAGE + status);
        }

        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        return BookingMapper.toResponseDto(booking, booking.getBooker(), item);
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
        return BookingMapper.toDetailedDto(booking);
    }

    @Override
    public List<BookingDetailedDto> findAllByBooker(String stateValue, Long userId, int from, int size) {
        checkIfUserExists(userId);
        State status = parseState(stateValue);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();

        Pageable pageable = PageRequest.of(from / size, size, SORT);

        switch (status) {
            case REJECTED :
                bookings = bookingRepository
                .findByBookerIdAndStatus(userId, REJECTED, pageable).toList();
                break;
            case WAITING :
                bookings = bookingRepository
                .findByBookerIdAndStatus(userId, WAITING, pageable).toList();
                break;
            case CURRENT :
                bookings = bookingRepository.findByBookerIdCurrent(userId, now, pageable).toList();
                break;
            case FUTURE :
                bookings = bookingRepository
                .findByBookerIdAndStartIsAfter(userId, now, pageable).toList();
                break;
            case PAST :
                bookings = bookingRepository
                .findByBookerIdAndEndIsBefore(userId, now, pageable).toList();
                break;
            case ALL :
                bookings = bookingRepository.findByBookerId(userId, pageable).toList();
                break;
        }
        return BookingMapper.toListDetailedDto(bookings);
    }

    @Override
    public List<BookingDetailedDto> findAllByItemOwner(String stateValue, Long userId, int from, int size) {
        checkIfUserExists(userId);
        State state = parseState(stateValue);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();

        Pageable pageable = PageRequest.of(from / size, size, SORT);

        switch (state) {
            case REJECTED :
                bookings = bookingRepository
                .findBookingByItemOwnerAndStatus(userId, REJECTED, pageable).toList();
                break;
            case WAITING :
                bookings = bookingRepository
                .findBookingByItemOwnerAndStatus(userId, WAITING, pageable).toList();
                break;
            case CURRENT :
                bookings = bookingRepository.findBookingsByItemOwnerCurrent(userId, now, pageable).toList();
                break;
            case FUTURE :
                bookings = bookingRepository
                .findBookingByItemOwnerAndStartIsAfter(userId, now, pageable).toList();
                break;
            case PAST :
                bookings = bookingRepository
                .findBookingByItemOwnerAndEndIsBefore(userId, now, pageable).toList();
                break;
            case ALL :
                bookings = bookingRepository
                .findBookingByItemOwner(userId, pageable).toList();
                break;
        }
        return BookingMapper.toListDetailedDto(bookings);
    }

    private void checkIfUserExists(Long userId) {
        userRepository.findById(userId).orElseThrow();
    }

    private State parseState(String state) {
        State status;
        try {
            status = State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException(ILLEGAL_SATE_MESSAGE + state);
        }
        return status;
    }

    private BookingStatus convertToStatus(Boolean approved) {
        return approved ? BookingStatus.APPROVED : REJECTED;
    }
}
package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBooker_IdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByBooker_Id(Long bookerId, Sort sort);

    //List<Booking> findBookingByBooker_IdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findBookingByItem_OwnerAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findBookingByItem_OwnerAndEndIsBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItem_OwnerAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItem_Owner(Long bookerId, Sort sort);
}

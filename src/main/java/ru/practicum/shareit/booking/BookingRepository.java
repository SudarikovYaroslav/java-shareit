package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBooker_IdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByBooker_Id(Long bookerId, Sort sort);

    List<Booking> findBookingByItem_OwnerAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findBookingByItem_OwnerAndEndIsBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItem_OwnerAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItem_Owner(Long bookerId, Sort sort);

    List<Booking> findBookingByItem_IdAndEndBefore(Long itemId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItem_IdAndStartAfter(Long itemId, LocalDateTime now, Sort sort);

    @Query("select b from bookings b " +
            "where b.booker.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            "order by b.start asc ")
    List<Booking> findByBookerIdCurrent(Long userId,LocalDateTime now);

    @Query("select b from bookings b " +
            "where b.item.owner = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            "order by b.start asc")
    List<Booking> findBookingsByItemOwnerCurrent(Long userId, LocalDateTime now);

    @Query("select b from bookings b " +
            " where b.item.id = ?1 " +
            " and b.booker.id = ?2" +
            " and b.end < ?3")
    List<Booking> findBookingsForAddComments(Long itemId, Long userId, LocalDateTime now);
}

package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "bookings")
public class Booking {
    public static final int MAX_STATUS_LENGTH = 64;
    public static final String END_COLUMN_NAME = "end";
    public static final String ITEM_COLUMN_NAME = "item";
    public static final String START_COLUMN_NAME = "start";
    public static final String ID_COLUMN_NAME = "booking_id";
    public static final String BOOKER_COLUMN_NAME = "booker";
    public static final String STATUS_COLUMN_NAME = "status";

    @Id
    @Column(name = ID_COLUMN_NAME)
    private Long id;
    @Column(name = START_COLUMN_NAME, nullable = false)
    private LocalDateTime start;
    @Column(name = END_COLUMN_NAME, nullable = false)
    private LocalDateTime end;
    @Column(name = ITEM_COLUMN_NAME, nullable = false)
    private Long item;
    @Column(name = BOOKER_COLUMN_NAME, nullable = false)
    private Long booker;
    @Column(name = STATUS_COLUMN_NAME, nullable = false, length = MAX_STATUS_LENGTH)
    private BookingStatus status;
}

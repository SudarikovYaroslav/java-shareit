package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validation_markers.Create;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingPostDto {
    private Long id;
    private Long itemId;
    @FutureOrPresent(groups = {Create.class})
    private LocalDateTime start;
    @FutureOrPresent(groups = {Create.class})
    private LocalDateTime end;
}

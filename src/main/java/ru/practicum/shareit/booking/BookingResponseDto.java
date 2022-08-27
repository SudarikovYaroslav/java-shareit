package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Long id;
    private BookingStatus status;
    private Long booker;
    private Long item;
    private String name;
}

/*
*     var jsonData = pm.response.json();
    pm.expect(jsonData.id, '"id" field').to.eql(1);
    pm.expect(jsonData.status, '"status" field').to.eql('APPROVED');
    pm.expect(jsonData.booker.id, '"booker.id" field').to.eql(1);
    pm.expect(jsonData.item.id, '"item.id" field').to.eql(2);
    pm.expect(jsonData.item.name, '"item.name" field').to.eql('Отвертка');
* */
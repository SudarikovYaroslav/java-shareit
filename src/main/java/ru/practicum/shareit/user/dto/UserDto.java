package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


//  в постман проверяется ид пользователя
@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String name;
    private String email;
}

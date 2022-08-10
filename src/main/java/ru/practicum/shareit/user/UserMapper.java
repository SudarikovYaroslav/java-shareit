package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Service
public class UserMapper {
    public UserDto convertUserIntoUserDto(User user) {
        return new UserDto(user.getName(), user.getEmail());
    }
}

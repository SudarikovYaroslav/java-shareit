package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {
    public static final Long NULL_USER_ID = null;

    public UserDto mapUserToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public User mapUserDtoToUser(UserDto userDto) {
        return new User(NULL_USER_ID, userDto.getName(), userDto.getName());
    }

    public List<UserDto> mapUserListToUserDtoList(List<User> users) {
        List<UserDto> result = new ArrayList<>();
        for (User user : users) {
            result.add(mapUserToUserDto(user));
        }
        return result;
    }
}

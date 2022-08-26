package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User updateUser(long userId, User user);

    User findUserById(long userId);

    void deleteUserById(long userId);

    List<User> findAllUsers();
}

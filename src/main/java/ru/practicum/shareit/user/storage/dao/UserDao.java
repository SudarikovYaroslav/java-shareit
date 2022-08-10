package ru.practicum.shareit.user.storage.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User createUser(User user);

    User updateUser(long userId, User user);

    User findUserByID(long userId);

    void deleteUserById(long userId);

    List<User> findAllUsers();
}

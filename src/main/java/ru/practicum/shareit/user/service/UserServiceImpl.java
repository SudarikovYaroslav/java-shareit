package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.dao.UserDao;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private UserValidationService validationService;

    @Override
    public User createUser(User user) {
        validationService.validateUser(user);
        return userDao.createUser(user);
    }

    @Override
    public User updateUser(long userId, User user) {
        validationService.validateUserId(userId);
        return userDao.updateUser(userId, user);
    }

    @Override
    public User findUserById(long userId) {
        validationService.validateUserId(userId);
        return userDao.findUserByID(userId);
    }

    @Override
    public void deleteUserById(long userId) {
        validationService.validateUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }
}

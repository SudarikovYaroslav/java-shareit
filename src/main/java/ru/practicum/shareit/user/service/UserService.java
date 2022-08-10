package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.dao.UserDao;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserDao userDao;
    private UserValidationService validationService;

    public User createUser(User user) {
        validationService.validateUser(user);
        return userDao.createUser(user);
    }

    public User updateUser(long userId, User user) {
        validationService.validateUserId(userId);
        return userDao.updateUser(userId, user);
    }

    public User findUserById(long userId) {
        validationService.validateUserId(userId);
        return userDao.findUserByID(userId);
    }

    public void deleteUserById(long userId) {
        validationService.validateUserId(userId);
        userDao.deleteUserById(userId);
    }

    public List<User> findAllUsers(){
        return userDao.findAllUsers();
    }
}

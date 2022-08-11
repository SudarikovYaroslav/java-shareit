package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exception.InvalidItemException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.OwnerNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.dao.UserDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemValidationService {

    public static final String OWNER_NULL_MESSAGE = "Не указан владелец вещи";
    public static final String INCORRECT_ID_MESSAGE = "Недопустимое значение id: ";
    public static final String EMPTY_NAME_MESSAGE = "Имя вещи не указано или пустое";
    public static final String OWNER_NOT_FOUND_MESSAGE = "Не найден владелец c id: ";
    public static final String AVAILABLE_NULL_MESSAGE = "Не указан статус доступности предмета";
    public static final String EMPTY_DESCRIPTION_MESSAGE = "Описание вещи не указано или пустое";

    private final UserDao userDao;

    public void validateItem(Item item) {
        checkOwnerNotNull(item);

        if (item.getAvailable() == null) {
            throw new InvalidItemException(AVAILABLE_NULL_MESSAGE);
        }

        String itemName = item.getName();
        if (itemName == null || itemName.isBlank()) {
            throw new InvalidItemException(EMPTY_NAME_MESSAGE);
        }

        String itemDescription = item.getDescription();
        if (itemDescription == null || itemDescription.isBlank()) {
            throw new InvalidItemException(EMPTY_DESCRIPTION_MESSAGE);
        }

        boolean ownerExists = checkIsOwnerExists(item.getOwner());
        if (!ownerExists) {
            throw new OwnerNotFoundException(OWNER_NOT_FOUND_MESSAGE + item.getOwner());
        }
    }

    public void checkOwnerNotNull(Item item) {
        if (item.getOwner() == null) throw new InvalidItemException(OWNER_NULL_MESSAGE);
    }

    public void validateItemId(Long itemId) {
        if (itemId == null || itemId <= 0) throw new ItemNotFoundException(INCORRECT_ID_MESSAGE);
    }

    public void validateUserId(Long userId) {
        if (userId == null || userId <= 0 || !checkIsOwnerExists(userId)) {
            throw new OwnerNotFoundException(OWNER_NOT_FOUND_MESSAGE + userId);
        }
    }

    private boolean checkIsOwnerExists(long ownerId) {
        List<User> users = userDao.findAllUsers();
        List<User> result = users.stream().filter(user -> user.getId() == ownerId).collect(Collectors.toList());
        return result.size() > 0;
    }
}

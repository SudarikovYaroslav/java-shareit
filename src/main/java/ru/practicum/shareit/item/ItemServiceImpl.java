package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DeniedAccessException;
import ru.practicum.shareit.exceptions.OwnerNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    public static final int MIN_SEARCH_REQUEST_LENGTH = 3;
    public static final String OWNER_NOT_FOUND_MESSAGE = "Не найден владелец c id: ";
    public static final String DENIED_ACCESS_MESSAGE = "Пользователь не является владельцем вещи";

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item createItem(Item item) {
        boolean ownerExists = isOwnerExists(item.getOwner());
        if (!ownerExists) {
            throw new OwnerNotFoundException(OWNER_NOT_FOUND_MESSAGE + item.getOwner());
        }
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item item) {
        return itemRepository.save(refreshItem(item));
    }

    @Override
    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow();
    }

    @Override
    public List<Item> findAllItems(Long userId) {
        return itemRepository.findAll()
                .stream().filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findItemsByRequest(String text) {
        if (text == null || text.isBlank() || text.length() <= MIN_SEARCH_REQUEST_LENGTH) {
            return new ArrayList<>();
        }
        return itemRepository.search(text);
    }

    private boolean isOwnerExists(long ownerId) {
        List<User> users = userRepository.findAll();
        List<User> result = users.stream().filter(user -> user.getId() == ownerId).collect(Collectors.toList());
        return result.size() > 0;
    }

    private Item refreshItem(Item patch) {
        Item entry = itemRepository.findById(patch.getId()).orElseThrow();

        if (!entry.getOwner().equals(patch.getOwner())) {
            throw new DeniedAccessException(DENIED_ACCESS_MESSAGE +
                    "userId: " + patch.getOwner() + ", itemId: " + patch.getId());
        }

        String name = patch.getName();
        if (name != null && !name.isBlank()) {
            entry.setName(name);
        }

        String description = patch.getDescription();
        if (description != null && !description.isBlank()) {
            entry.setDescription(description);
        }

        Boolean available = patch.getAvailable();
        if (available != null) {
            entry.setAvailable(available);
        }
        return entry;
    }
}

package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private Item item1;

    @Autowired
    public void beforeEach() {
        user1 = userRepository.save(new User(1L, "user 1", "user1@email.com"));

        item1 = itemRepository.save(
                new Item(1L,
                "item 1",
                "description",
                true,
                user1.getId(),
                null));
    }

    @Test
    public void searchTest() {
        Page<Item> result = itemRepository.search("description", Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        Item founded = result.getContent().get(0);
        assertEquals(founded.getId(), item1.getId());
        assertEquals(founded.getName(), item1.getName());
        assertEquals(founded.getDescription(), item1.getDescription());
    }

    @Test
    public void findAllTest() {
        Page<Item> result = itemRepository.findAll(user1.getId(), Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        Item founded = result.getContent().get(0);
        assertEquals(founded.getId(), item1.getId());
        assertEquals(founded.getName(), item1.getName());
        assertEquals(founded.getDescription(), item1.getDescription());
    }

    @Test
    public void findAllByRequestIdTest() {
        List<Item> result = itemRepository.findAllByRequestId(1L);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @AfterEach
    public void afterEach() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}

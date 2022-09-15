package ru.practicum.shareit.requests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private Request request;

    @BeforeEach
    public void beforeEach() {
        user1 = userRepository.save(new User(null, "user 1", "user1@email.com"));
        user2 = userRepository.save(new User(null, "user 2", "user2@email.com"));
        request = itemRequestRepository.save(new Request(
                null,
                "request",
                user2.getId(),
                LocalDateTime.now()));
    }

    @Test
    public void findRequestByRequestorOrderByCreatedDescTest() {
        List<Request> result = itemRequestRepository
                .findRequestByRequestorOrderByCreatedDesc(user1.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(request, result.get(0));
    }

    @Test
    public void findAll() {
        Page<Request> result = itemRequestRepository.findAll(user1.getId(), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(request, result.getContent().get(0));
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }
}

package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.requests.Request;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository requestRepository;

    private Item item;
    private User itemOwner;
    private User requestor;
    private Comment comment;
    private Request request;

    @BeforeEach
    public void beforeEach() {
        LocalDateTime date = LocalDateTime.now();
        itemOwner = userRepository.save(new User(null, "itemOwner", "itemOwner@email.com"));
        requestor = userRepository.save(new User(null, "requestor", "requestor@email.com"));
        request = requestRepository.save(new Request(null, "description", requestor.getId(), date));

        item = itemRepository.save(
                new Item(null,
                        "item",
                        "description",
                        true,
                        itemOwner.getId(),
                        null));

        comment = commentRepository.save(new Comment(null, "comment", item, itemOwner, LocalDateTime.now()));
    }

    @Test
    public void findByItemIdTest() {
        List<Comment> result = commentRepository.findByItemId(item.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(comment.getId(), result.get(0).getId());
        assertEquals(comment.getText(), result.get(0).getText());
        assertEquals(comment.getAuthor(), itemOwner);
        assertEquals(comment.getItem(), item);
    }

    @AfterEach
    public void afterEach() {
        commentRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();
        requestRepository.deleteAll();
    }
}

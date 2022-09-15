package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
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

    private User user1;
    private Item item1;
    private Comment comment;

    @BeforeEach
    public void beforeEach() {
        user1 = userRepository.save(new User(1L, "user 1", "user1@email.com"));

        item1 = itemRepository.save(
                new Item(1L,
                        "item 1",
                        "description",
                        true,
                        user1.getId(),
                        null));

        comment = commentRepository.save(new Comment(1L, "comment", item1, user1, LocalDateTime.now()));
    }

    @Test
    public void findByItemIdTest() {
        List<Comment> result = commentRepository.findByItemId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(comment.getId(), result.get(0).getId());
        assertEquals(comment.getText(), result.get(0).getText());
        assertEquals(comment.getAuthor(), user1);
        assertEquals(comment.getItem(), item1);
    }

    @AfterEach
    public void afterEach() {
        commentRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }
}

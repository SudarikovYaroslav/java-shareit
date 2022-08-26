package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comments")
public class Comment {
    public static final int MAX_TEXT_LENGTH = 512;
    public static final String TEXT_COLUMN_NAME = "text";
    public static final String ITEM_COLUMN_NAME = "item";
    public static final String OWNER_COLUMN_NAME = "owner";
    public static final String ID_COLUMN_NAME = "comment_id";

    @Id
    @Column(name = ID_COLUMN_NAME)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = OWNER_COLUMN_NAME, nullable = false)
    private Long owner;
    @Column(name = ITEM_COLUMN_NAME, nullable = false)
    private Long item;
    @Column(name = TEXT_COLUMN_NAME, nullable = false, length = MAX_TEXT_LENGTH)
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id != null
                && Objects.equals(id, comment.id)
                && Objects.equals(owner, comment.owner)
                && Objects.equals(item, comment.item)
                && Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, item, text);
    }
}

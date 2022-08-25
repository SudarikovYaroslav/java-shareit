package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
    private Long id;
    @Column(name = OWNER_COLUMN_NAME, nullable = false)
    private Long owner;
    @Column(name = ITEM_COLUMN_NAME, nullable = false)
    private Long item;
    @Column(name = TEXT_COLUMN_NAME, nullable = false, length = MAX_TEXT_LENGTH)
    private String text;
}

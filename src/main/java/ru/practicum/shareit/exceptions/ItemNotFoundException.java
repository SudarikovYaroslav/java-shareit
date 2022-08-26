package ru.practicum.shareit.exceptions;

import java.util.function.Supplier;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}

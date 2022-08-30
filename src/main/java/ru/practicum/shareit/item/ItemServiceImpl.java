package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.DeniedAccessException;
import ru.practicum.shareit.exceptions.OwnerNotFoundException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    public static final int MIN_SEARCH_REQUEST_LENGTH = 3;
    public static final String OWNER_NOT_FOUND_MESSAGE = "Не найден владелец c id: ";
    public static final String DENIED_ACCESS_MESSAGE = "Пользователь не является владельцем вещи";

    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        Item item = itemMapper.toModel(itemDto, userId);
        boolean ownerExists = isOwnerExists(item.getOwner());
        if (!ownerExists) {
            throw new OwnerNotFoundException(OWNER_NOT_FOUND_MESSAGE + item.getOwner());
        }
        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto createComment(CommentCreateDto dto, Long itemId, Long userId) {
        return null;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        Item item = itemMapper.toModel(itemDto, userId);
        item.setId(itemId);
        return itemMapper.toDto(itemRepository.save(refreshItem(item)));
    }

    @Override
    public ItemDto findItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow();

        if (item.getOwner().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();
            Sort sortDesc = Sort.by("start").descending();

            Booking lastBooking = bookingRepository.findBookingByItem_OwnerAndEndIsBefore(userId, now, sortDesc)
                    .stream().findFirst().orElse(null);
            Booking nextBooking = bookingRepository.findBookingByItem_OwnerAndStartIsAfter(userId, now, sortDesc)
                    .stream().findFirst().orElse(null);

            return itemMapper.toDto(item,
                    bookingMapper.bookingInItemDto(lastBooking),
                    bookingMapper.bookingInItemDto(nextBooking));
        }
        return itemMapper.toDto(item, null, null);
    }

    @Override
    public List<ItemDto> findAllItems(Long userId) {
        List<Item> userItems = itemRepository.findAll()
                .stream().filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();
        Sort sortDesc = Sort.by("start").descending();

        List<ItemDto> result = new ArrayList<>();
        for (Item item : userItems) {
            if (item.getOwner().equals(userId)) {
                //findBookingByItem_OwnerAndEndIsBefore(userId, now, sortDesc)
                Booking lastBooking = bookingRepository.findBookingByItem_IdAndEndBefore(item.getId(), now, sortDesc)
                    .stream().findFirst().orElse(null);
                //findBookingByItem_OwnerAndStartIsAfter(userId, now, sortDesc)
                Booking nextBooking = bookingRepository.findBookingByItem_IdAndStartAfter(item.getId(), now, sortDesc)
                    .stream().findFirst().orElse(null);

                ItemDto dto = itemMapper.toDto(item,
                        bookingMapper.bookingInItemDto(lastBooking),
                        bookingMapper.bookingInItemDto(nextBooking));
                result.add(dto);
            } else {
                result.add(itemMapper.toDto(item));
            }
        }
        result.sort((o1, o2) -> {
            if (o1.getNextBooking() == null && o2.getNextBooking() == null) {return 0; }
            if (o1.getNextBooking() != null && o2.getNextBooking() == null) { return -1; }
            if (o1.getNextBooking() == null && o2.getNextBooking() != null) { return 1; }
            if (o1.getNextBooking().getStart().isBefore(o2.getNextBooking().getStart())) { return -1; }
            if (o1.getNextBooking().getStart().isAfter(o2.getNextBooking().getStart())) { return 1; }
            return 0;
        });
        return result;
    }

    @Override
    public List<ItemDto> findItemsByRequest(String text) {
        if (text == null || text.isBlank() || text.length() <= MIN_SEARCH_REQUEST_LENGTH) {
            return new ArrayList<>();
        }
        List<Item> foundItems = itemRepository.search(text);
        return itemMapper.mapItemListToItemDtoList(foundItems);
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

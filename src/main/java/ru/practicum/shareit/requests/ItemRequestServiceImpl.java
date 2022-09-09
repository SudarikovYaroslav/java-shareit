package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.dto.RequestWithItemsDto;
import ru.practicum.shareit.requests.dto.PostRequestDto;
import ru.practicum.shareit.requests.dto.PostResponseRequestDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    public static final int DEFAULT_FROM_VALUE = 0;
    public static final int DEFAULT_SIZE_VALUE = 20;
    public static final Sort SORT = Sort.by("created").descending();

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public PostResponseRequestDto createRequest(PostRequestDto dto, Long userId) {
        checkIfUserExists(userId);
        Request request  = RequestMapper.toModel(dto, userId);
        request = requestRepository.save(request);
        return RequestMapper.toPostResponseDto(request);
    }

    @Override
    public List<RequestWithItemsDto> findAllByUserId(Long userId) {
        checkIfUserExists(userId);
        List<Request> requests = requestRepository.findRequestByRequestorOrderByCreatedDesc(userId);
        return RequestMapper.toRequestWithItemsDtoList(requests, itemRepository);
    }

    //TODO падает тест хотя страница приходит пустая
    @Override
    public Page<RequestWithItemsDto> findAll(int from, int size, Long userId) {
        checkIfUserExists(userId);
        Pageable pageable = PageRequest.of(from, size, SORT);
        Page<Request> requests = requestRepository.findAll(pageable);
        Page<RequestWithItemsDto> result = RequestMapper.toRequestWithItemsDtoPage(requests, itemRepository);
        return result.isEmpty() ? Page.empty() : result;
    }

    @Override
    public RequestWithItemsDto findById(Long requestId, Long userId) {
        checkIfUserExists(userId);
        Request request = requestRepository.findById(requestId).orElseThrow();
        List<Item> items = itemRepository.findAllByRequestId(requestId);
        return RequestMapper.toRequestWithItemsDto(request, items);
    }

    private void checkIfUserExists(Long userId) {
        userRepository.findById(userId).orElseThrow();
    }
}

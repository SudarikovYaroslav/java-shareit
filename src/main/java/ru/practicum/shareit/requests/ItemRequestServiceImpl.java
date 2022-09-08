package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.RequestNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.dto.RequestWithItemsDto;
import ru.practicum.shareit.requests.dto.PostRequestDto;
import ru.practicum.shareit.requests.dto.PostResponseRequestDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    public static final String REQUEST_NOT_FOUND_MESSAGE = "не найден запрос id: ";

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
    public List<RequestWithItemsDto> findAll(Long userId) {
        checkIfUserExists(userId);
        List<RequestWithItemsDto> result = new ArrayList<>();
        List<Request> requests = requestRepository.findRequestByRequestorOrderByCreatedDesc(userId);

        if (requests != null && !requests.isEmpty()) {
            for (Request request : requests) {
                List<Item> items = itemRepository.findAllByRequestId(request.getId());
                RequestWithItemsDto dto = RequestMapper.toRequestWithItemsDto(request, items);
                result.add(dto);
            }
        }
        return result;
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

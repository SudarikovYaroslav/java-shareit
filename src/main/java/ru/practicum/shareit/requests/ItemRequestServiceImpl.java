package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.InRequestItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.requests.dto.DetailedResponseRequestDto;
import ru.practicum.shareit.requests.dto.PostRequestDto;
import ru.practicum.shareit.requests.dto.PostResponseRequestDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public PostResponseRequestDto createRequest(PostRequestDto dto, Long userId) {
        checkIfUserExists(userId);
        Request request  = RequestMapper.toModel(dto, userId);
        request = requestRepository.save(request);
        return RequestMapper.toPostResponseDto(request);
    }

    //TODO сделать кастомный запрос в БД и маппинг.
    // Похоже для начала нужно реализовать  опцию ответа на запрос,
    // а то совсем не понятно, каким образом собрать список вещей по запросу
    @Override
    public List<DetailedResponseRequestDto> find(Long userId) {
        checkIfUserExists(userId);
        List<DetailedResponseRequestDto> result = new ArrayList<>();
        List<Request> requests = requestRepository.findRequestByRequestorOrderByCreatedDesc(userId);

        if (requests != null && !requests.isEmpty()) {

            for (Request request : requests) {
                List<Item> items = new ArrayList<>(); //TODO получить как то вещи по ответам из БД вместо new List
                List<InRequestItemDto> requestItemDtos = ItemMapper.toInRequestItemDtoList(items);
                DetailedResponseRequestDto dto = RequestMapper.toDetailedResponseRequestDto(request, requestItemDtos);
                result.add(dto);
            }
        }
        return result;
    }

    private void checkIfUserExists(Long userId) {
        userRepository.findById(userId).orElseThrow();
    }
}

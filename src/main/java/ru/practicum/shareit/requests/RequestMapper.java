package ru.practicum.shareit.requests;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.dto.PostRequestDto;
import ru.practicum.shareit.requests.dto.PostResponseRequestDto;
import ru.practicum.shareit.requests.dto.RequestWithItemsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    public static Request toModel(PostRequestDto dto, Long requestor) {
        Request request = new Request();
        request.setDescription(dto.getDescription());
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());
        return request;
    }

    public static PostResponseRequestDto toPostResponseDto(Request request) {
        PostResponseRequestDto dto = new PostResponseRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        return dto;
    }

    public static RequestWithItemsDto toRequestWithItemsDto(Request request, List<Item> items) {
        List<ItemInRequestDto> itemDtos = ItemMapper.toRequestItemDtoList(items);
        RequestWithItemsDto dto = new RequestWithItemsDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setItems(itemDtos);
        return dto;
    }

    public static Page<RequestWithItemsDto> toRequestWithItemsDtoPage(Page<Request> requests,
                                                                      ItemRepository repository) {
        return requests.map((Request request)-> {
            List<Item> items = repository.findAllByRequestId(request.getId());
            return RequestMapper.toRequestWithItemsDto(request, items);
        });
    }

    @Deprecated
    public static List<RequestWithItemsDto> toRequestWithItemsDtoList(List<Request> requests, ItemRepository repository) {
        List<RequestWithItemsDto> result = new ArrayList<>();
        if (requests != null && !requests.isEmpty()) {
            for (Request request : requests) {
                List<Item> items = repository.findAllByRequestId(request.getId());
                RequestWithItemsDto requestDto = RequestMapper.toRequestWithItemsDto(request, items);
                result.add(requestDto);
            }
        }
        return result;
    }
}
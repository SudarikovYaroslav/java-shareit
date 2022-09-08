package ru.practicum.shareit.requests;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.InRequestItemDto;
import ru.practicum.shareit.requests.dto.DetailedResponseRequestDto;
import ru.practicum.shareit.requests.dto.PostRequestDto;
import ru.practicum.shareit.requests.dto.PostResponseRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    // TODO need realisation
    public static DetailedResponseRequestDto toDetailedResponseRequestDto(Request request,
                                                                          List<InRequestItemDto> items) {
        DetailedResponseRequestDto dto = new DetailedResponseRequestDto();
        return dto;
    }
}

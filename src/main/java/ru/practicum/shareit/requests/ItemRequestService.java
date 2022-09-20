package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.dto.PostRequestDto;
import ru.practicum.shareit.requests.dto.PostResponseRequestDto;
import ru.practicum.shareit.requests.dto.RequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {

    PostResponseRequestDto createRequest(PostRequestDto dto, Long userId);

    List<RequestWithItemsDto> findAllByUserId(Long userId);

    List<RequestWithItemsDto> findAll(int from, int size, Long userId);

    RequestWithItemsDto findById(Long requestId, Long userId);
}

package ru.practicum.shareit.requests;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.requests.dto.PostRequestDto;
import ru.practicum.shareit.requests.dto.PostResponseRequestDto;
import ru.practicum.shareit.requests.dto.RequestWithItemsDto;

public interface ItemRequestService {

    PostResponseRequestDto createRequest(PostRequestDto dto, Long userId);

    Page<RequestWithItemsDto> findAllByUserId(Long userId);

    Page<RequestWithItemsDto> findAll(int from, int size, Long userId);

    RequestWithItemsDto findById(Long requestId, Long userId);
}

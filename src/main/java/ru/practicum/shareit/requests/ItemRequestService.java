package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.dto.DetailedResponseRequestDto;
import ru.practicum.shareit.requests.dto.PostRequestDto;
import ru.practicum.shareit.requests.dto.PostResponseRequestDto;

import java.util.List;

public interface ItemRequestService {

    PostResponseRequestDto createRequest(PostRequestDto dto, Long userId);

    List<DetailedResponseRequestDto> find(Long userId);

}

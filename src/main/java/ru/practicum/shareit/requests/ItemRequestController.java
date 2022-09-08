package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.RequestWithItemsDto;
import ru.practicum.shareit.requests.dto.PostRequestDto;
import ru.practicum.shareit.requests.dto.PostResponseRequestDto;
import ru.practicum.shareit.validation_markers.Create;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    public static final int MIN_ID_VALUE = 1;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemRequestService service;

    @PostMapping
    public PostResponseRequestDto createRequest(@Validated({Create.class})
                                                @RequestBody PostRequestDto postRequestDto,
                                                @RequestParam(USER_ID_HEADER) Long userId) {
        return service.createRequest(postRequestDto, userId);
    }

    @GetMapping
    public List<RequestWithItemsDto> findAll(@RequestParam(USER_ID_HEADER) Long userId) {
        return service.findAll(userId);
    }

    @GetMapping ("/all")
    public List<PostResponseRequestDto> findAll() {
        return null;
    }

    @GetMapping("/{requestId}")
    public RequestWithItemsDto findById(@Min(MIN_ID_VALUE)
                                           @PathVariable Long requestId,
                                           @RequestParam(USER_ID_HEADER) Long userId) {
        return service.findById(requestId, userId);
    }
}

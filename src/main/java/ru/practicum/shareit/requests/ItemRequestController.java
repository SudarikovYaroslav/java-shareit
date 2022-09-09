package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.PostRequestDto;
import ru.practicum.shareit.requests.dto.PostResponseRequestDto;
import ru.practicum.shareit.requests.dto.RequestWithItemsDto;
import ru.practicum.shareit.validation_markers.Create;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    public static final int MIN_VALUE = 1;
    public static final String DEFAULT_FROM_VALUE = "1";
    public static final String DEFAULT_SIZE_VALUE = "20";
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemRequestService service;

    @PostMapping
    public PostResponseRequestDto createRequest(@Validated({Create.class})
                                                @RequestBody PostRequestDto postRequestDto,
                                                @RequestHeader(USER_ID_HEADER) Long userId) {
        return service.createRequest(postRequestDto, userId);
    }

    @GetMapping
    public List<RequestWithItemsDto> findAllByUserId(@RequestHeader(USER_ID_HEADER) Long userId) {
        return service.findAllByUserId(userId);
    }

    //TODO добавить пагинацию
    @GetMapping ("/all")
    public Page<RequestWithItemsDto> findAll(@Positive
                                             @Min(MIN_VALUE)
                                             @RequestParam(defaultValue = DEFAULT_FROM_VALUE) int from,
                                             @Positive
                                             @Min(MIN_VALUE)
                                             @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) int size,
                                             @RequestHeader(USER_ID_HEADER) Long userId) {
        return service.findAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public RequestWithItemsDto findById(@Min(MIN_VALUE)
                                        @PathVariable Long requestId,
                                        @RequestHeader(USER_ID_HEADER) Long userId) {
        return service.findById(requestId, userId);
    }
}

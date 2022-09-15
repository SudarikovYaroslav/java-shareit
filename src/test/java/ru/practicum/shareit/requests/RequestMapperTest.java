package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.dto.PostRequestDto;
import ru.practicum.shareit.requests.dto.PostResponseRequestDto;
import ru.practicum.shareit.requests.dto.RequestWithItemsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class RequestMapperTest {

    public static final long ID = 1L;
    public static final int FROM_VALUE = 0;
    public static final int SIZE_VALUE = 20;
    public static final LocalDateTime CREATED_DATE = LocalDateTime.now();

    private Request request;
    private PostRequestDto postRequestDto;
    private ItemRepository itemRepository;

    @BeforeEach
    public void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        request = new Request(ID, "description", ID, CREATED_DATE);
        postRequestDto = new PostRequestDto("description");
    }

    @Test
    public void toModelTest() {
        Request result = RequestMapper.toModel(postRequestDto, ID);

        assertNotNull(result);
        assertEquals(ID, result.getRequestor());
        assertEquals(postRequestDto.getDescription(), result.getDescription());
    }

    @Test
    public void toPostResponseDtoTest() {
        PostResponseRequestDto result = RequestMapper.toPostResponseDto(request);

        assertNotNull(result);
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getCreated(), result.getCreated());
    }

    @Test
    public void toRequestWithItemsDtoTest() {
        RequestWithItemsDto result = RequestMapper.toRequestWithItemsDto(request, new ArrayList<>());

        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getCreated(), result.getCreated());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void toRequestWithItemsDtoListTest() {
        List<Request> requests = Collections.singletonList(request);
        Page<Request> page = new PageImpl<>(requests);

        List<RequestWithItemsDto> fromList = RequestMapper.toRequestWithItemsDtoList(page, itemRepository);
        List<RequestWithItemsDto> fromPage = RequestMapper.toRequestWithItemsDtoList(requests, itemRepository);

        assertNotNull(fromList);
        assertNotNull(fromPage);
        assertEquals(request.getId(), fromList.get(0).getId());
        assertEquals(request.getId(), fromPage.get(0).getId());
        assertTrue(fromList.get(0).getItems().isEmpty());
        assertTrue(fromPage.get(0).getItems().isEmpty());
    }

}

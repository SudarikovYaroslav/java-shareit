package ru.practicum.shareit.requests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<Request, Long> {

    List<Request> findRequestByRequestorOrderByCreatedDesc(Long requestor);
}

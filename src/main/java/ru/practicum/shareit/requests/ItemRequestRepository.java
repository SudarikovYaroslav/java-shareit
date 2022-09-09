package ru.practicum.shareit.requests;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<Request, Long> {

    Page<Request> findRequestsByRequestor(Long requestor, Pageable pageable);

    List<Request> findRequestByRequestorOrderByCreatedDesc(Long requestor);
}

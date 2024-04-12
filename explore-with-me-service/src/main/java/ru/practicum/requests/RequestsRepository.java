package ru.practicum.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.Status;
import ru.practicum.model.request.Request;

import java.util.List;
import java.util.Optional;

public interface RequestsRepository extends JpaRepository<Request, Long> {

    List<Request> findByEvent_Initiator_IdNotAndRequester(long userId, long userId2);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Request r set r.status = :status where r.id = :id")
    void updateStatus(@Param("status") Status status, @Param("id") long id);

    Optional<Request> findByRequesterAndEvent_Id(long userId, long eventId);

    List<Request> findByEvent_id(long eventId);

    List<Request> findByIdIn(List<Long> ids);
}

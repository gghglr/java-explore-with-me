package ru.practicum.event.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import ru.practicum.dto.event.State;
import ru.practicum.model.event.UserEvent;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserEventRepository extends JpaRepository<UserEvent, Long> {

    List<UserEvent> findByInitiator_Id(long userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE UserEvent r set r.confirmedRequests = r.confirmedRequests + :count where r.id = :id")
    void plusConfirmedRequests(@Param("id") long id, @Param("count") int count);

    @Transactional
    @Modifying
    @Query("UPDATE UserEvent r set r.confirmedRequests = r.confirmedRequests - :count where r.id = :id")
    void minusConfirmedRequests(@Param("id") long id, @Param("count") int count);

    @Query(value = "SELECT e.confirmedRequests FROM UserEvent e WHERE e.id = :id")
    int getConfirmedReq(@Param("id") long id);

    List<UserEvent> findByIdIn(List<Long> ids);

    Optional<UserEvent> findByCategory_Id(long catId);

    @Query(value = "SELECT e FROM UserEvent e " +
            "WHERE 1 = 1 " +
            "AND (e.category.id in :categories or :categories is null) " +
            "AND (e.initiator.id in :users or :users is null) " +
            "AND (e.state in :states or :states is null) " +
            "AND (e.eventDate BETWEEN :start AND :end)")
    Page<UserEvent> findByFilter(@Nullable @Param("users") List<Long> users,
                                 @Nullable @Param("states") List<State> states,
                                 @Nullable @Param("categories") List<Long> categories,
                                 @Param("start") LocalDateTime rangeStart,
                                 @Param("end") LocalDateTime rangeEnd,
                                 Pageable page);

    Page<UserEvent> findByEventDateBetween(LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query(value = "SELECT e FROM UserEvent e WHERE " +
            "(:text = '' OR (UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%')) OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND (:paid IS NULL OR e.paid IS :paid) AND (e.category.id IN :categories) " +
            "AND (e.eventDate BETWEEN :start AND :end) AND (:paid IS NULL OR e.paid IS :paid) " +
            "OR  (e.participantLimit - e.confirmedRequests) < 0)")
    Page<UserEvent> getEventsTrueWithCategories(@Param("text") String text,
                                                @Param("paid") Boolean paid,
                                                @Param("categories") List<Long> categories,
                                                @Param("start") LocalDateTime rangeStart,
                                                @Param("end") LocalDateTime rangeEnd,
                                                Pageable pageable);

    @Query(value = "SELECT e FROM UserEvent e WHERE " +
            "(:text LIKE '' OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%')) OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND (:paid IS NULL OR e.paid IS :paid) AND (e.category.id IN :categories) AND (e.eventDate BETWEEN :start AND :end)")
    Page<UserEvent> getEventsFalseWithCategories(@Param("text") String text,
                                                 @Param("paid") Boolean paid,
                                                 @Param("categories") List<Long> categories,
                                                 @Param("start") LocalDateTime rangeStart,
                                                 @Param("end") LocalDateTime rangeEnd,
                                                 Pageable pageable);

    @Query(value = "SELECT e FROM UserEvent e WHERE " +
            "(:text = '' OR (UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%')) OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND (:paid IS NULL OR e.paid IS :paid) AND (e.eventDate BETWEEN :start AND :end) " +
            "AND (e.participantLimit > e.confirmedRequests) OR  (e.participantLimit - e.confirmedRequests) < 0)")
    Page<UserEvent> getEventsTrueWithoutCategories(@Param("text") String text,
                                                   @Param("paid") Boolean paid,
                                                   @Param("start") LocalDateTime rangeStart,
                                                   @Param("end") LocalDateTime rangeEnd,
                                                   Pageable pageable);

    @Query(value = "SELECT e FROM UserEvent e WHERE " +
            "(:text LIKE '' OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%')) OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND (:paid IS NULL OR e.paid IS :paid) AND (e.eventDate BETWEEN :start AND :end)")
    Page<UserEvent> getEventsFalseWithoutCategories(@Param("text") String text,
                                                    @Param("paid") Boolean paid,
                                                    @Param("start") LocalDateTime rangeStart,
                                                    @Param("end") LocalDateTime rangeEnd,
                                                    Pageable pageable);


}

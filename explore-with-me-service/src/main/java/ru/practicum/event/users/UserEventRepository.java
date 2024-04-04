package ru.practicum.event.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.event.State;
import ru.practicum.model.event.UserEvent;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

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

    List<UserEvent> findByIdIn(List<Long> ids);

    @Query(value = "SELECT e FROM UserEvent e WHERE e.eventDate BETWEEN :start AND :end")
    Page<UserEvent> getForAdminWithoutParam(@Param("start") LocalDateTime rangeStart,
                                            @Param("end") LocalDateTime rangeEnd, Pageable page);

    @Query(value = "SELECT e FROM UserEvent e WHERE e.category.id IN :categories AND e.eventDate" +
            " BETWEEN :start AND :end")
    Page<UserEvent> getForAdminWithCategories(@Param("categories") List<Long> categories,
                                              @Param("start") LocalDateTime rangeStart,
                                              @Param("end") LocalDateTime rangeEnd, Pageable page);

    @Query(value = "SELECT e FROM UserEvent e WHERE e.state IN :states AND e.eventDate BETWEEN :start AND :end")
    Page<UserEvent> getForAdminStates(@Param("states") List<State> states,
                                                @Param("start") LocalDateTime rangeStart,
                                                @Param("end") LocalDateTime rangeEnd, Pageable page);

    @Query(value = "SELECT e FROM UserEvent e WHERE e.state IN :states AND e.category.id IN :categories AND e.eventDate BETWEEN :start AND :end")
    Page<UserEvent> getForAdminCategoriesAndStates(@Param("categories") List<Long> categories,
                                                             @Param("states") List<State> states,
                                                             @Param("start") LocalDateTime rangeStart,
                                                             @Param("end") LocalDateTime rangeEnd,
                                                             Pageable page);

    @Query(value = "SELECT e FROM UserEvent e WHERE e.initiator.id IN :users AND e.eventDate BETWEEN :start AND :end")
    Page<UserEvent> getForAdminUsers(@Param("users") List<Long> users,
                                               @Param("start") LocalDateTime rangeStart,
                                               @Param("end") LocalDateTime rangeEnd,
                                               Pageable page);

    @Query(value = "SELECT e FROM UserEvent e WHERE e.initiator.id IN :users AND e.category.id IN :categories " +
            "AND e.eventDate BETWEEN :start AND :end")
    Page<UserEvent> getForAdminUsersCategories(@Param("users") List<Long> users,
                                                            @Param("categories") List<Long> categories,
                                                            @Param("start") LocalDateTime rangeStart,
                                                            @Param("end") LocalDateTime rangeEnd,
                                                            Pageable page);

    @Query(value = "SELECT e FROM UserEvent e WHERE e.initiator.id IN :users AND e.state IN :states " +
            "AND e.eventDate BETWEEN :start AND :end")
    Page<UserEvent> getForAdminUsersStates(@Param("users") List<Long> users,
                                                        @Param("states") List<State> states,
                                                        @Param("start") LocalDateTime rangeStart,
                                                        @Param("end") LocalDateTime rangeEnd,
                                                        Pageable page);

    Page<UserEvent> findByInitiator_IdInAndStateInAndCategory_IdInAndEventDateBetween(List<Long> users,
                                                                                      List<State> states,
                                                                                      List<Long> categories,
                                                                                      LocalDateTime rangeStart,
                                                                                      LocalDateTime rangeEnd,
                                                                                      Pageable page);

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

package ru.practicum.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.likes.Mark;

import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    Optional<Mark> findByEventIdAndUserId(long eventId, long userId);

    Optional<Mark> findByEventIdAndUserIdAndMark(long eventId, long userId, boolean isLike);

    void deleteByEventIdAndUserIdAndMark(long eventId, long userId, Boolean isLike);
}

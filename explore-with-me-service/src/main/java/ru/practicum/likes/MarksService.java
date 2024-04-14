package ru.practicum.likes;

import ru.practicum.dto.mark.MarkDto;

public interface MarksService {

    MarkDto createLike(long userId, long eventId);

    MarkDto createDislike(long userId, long eventId);
    void deleteDislike(long userId, long eventId);
    void deleteLike(long userId, long eventId);
}

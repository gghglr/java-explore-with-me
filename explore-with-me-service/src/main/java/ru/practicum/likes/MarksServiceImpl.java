package ru.practicum.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.State;
import ru.practicum.dto.mark.MarkDto;
import ru.practicum.event.users.UserEventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.model.event.UserEvent;
import ru.practicum.model.likes.Mark;
import ru.practicum.users.admin.AdminRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarksServiceImpl implements MarksService {

    private final MarkRepository markRepository;
    private final AdminRepository userRepository;
    private final UserEventRepository eventRepository;

    @Override
    public MarkDto createLike(long userId, long eventId) {
        UserEvent event = validAndReturnEvent(userId, eventId);
        if (markRepository.findByEventIdAndUserId(eventId, userId).isPresent()) {
            throw new ConflictException("Оценка уже поставлена!");
        }
        eventRepository.setLike(eventId);
        int rating = event.getRating();
        Mark mark = new Mark();
        mark.setEventId(eventId);
        mark.setUserId(userId);
        mark.setMark(true);
        markRepository.save(mark);
        if (rating + 1 >= 5) {
            eventRepository.setRating(eventId, 5);
        } else {
            eventRepository.setRating(eventId, rating + 1);
        }
        return new MarkDto("лайков", eventRepository.getLikes(eventId));
    }

    @Override
    public MarkDto createDislike(long userId, long eventId) {
        UserEvent event = validAndReturnEvent(userId, eventId);
        if (markRepository.findByEventIdAndUserId(eventId, userId).isPresent()) {
            throw new ConflictException("Оценка уже поставлена!");
        }
        eventRepository.setDislike(eventId);
        int rating = event.getRating();
        Mark mark = new Mark();
        mark.setEventId(eventId);
        mark.setUserId(userId);
        mark.setMark(false);
        markRepository.save(mark);
        if (rating - 1 <= 0) {
            eventRepository.setRating(eventId, 0);
        } else {
            eventRepository.setRating(eventId, rating - 1);
        }
        return new MarkDto("дизлайков", eventRepository.getDislike(eventId));
    }

    @Transactional
    @Override
    public void deleteDislike(long userId, long eventId) {
        UserEvent event = validAndReturnEvent(userId, eventId);
        if (markRepository.findByEventIdAndUserIdAndMark(eventId, userId, false).isEmpty()) {
            throw new ConflictException("Нечего удалять, вы не ставили дизлайк этому событию!");
        }
        markRepository.deleteByEventIdAndUserIdAndMark(eventId, userId, false);
        int rating = event.getRating();
        eventRepository.removeDislike(eventId);
        if (rating + 1 >= 5) {
            eventRepository.setRating(eventId, 5);
        } else if (rating == 0) {
            eventRepository.setRating(eventId, 0);
        } else {
            eventRepository.setRating(eventId, rating + 1);
        }
    }

    @Transactional
    @Override
    public void deleteLike(long userId, long eventId) {
        UserEvent event = validAndReturnEvent(userId, eventId);
        if (!markRepository.findByEventIdAndUserIdAndMark(eventId, userId, true).isPresent()) {
            throw new ConflictException("Нечего удалять, вы не ставили лайк этому событию!");
        }
        markRepository.deleteByEventIdAndUserIdAndMark(eventId, userId, true);
        int rating = event.getRating();
        eventRepository.removeLike(eventId);
        if (rating - 1 <= 0) {
            eventRepository.setRating(eventId, 0);
        } else {
            eventRepository.setRating(eventId, rating - 1);
        }
    }

    private UserEvent validAndReturnEvent(long userId, long eventId) {
        boolean isUser = userRepository.existsById(userId);
        Optional<UserEvent> event = eventRepository.findById(eventId);
        if (!isUser) {
            throw new ConflictException("Пользователь, который пытается поставить лайк не существует!");
        }
        if (event.isEmpty()) {
            throw new ConflictException("Событие не существует!");
        }
        if (!event.get().getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Событие не опубликовано!");
        }
        return event.get();
    }

}

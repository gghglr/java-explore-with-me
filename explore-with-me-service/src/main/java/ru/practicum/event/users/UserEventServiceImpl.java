package ru.practicum.event.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.categories.admin.CategoryRepository;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.Status;
import ru.practicum.exception.BadRequest;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.categories.Category;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestMapper;
import ru.practicum.requests.RequestsRepository;
import ru.practicum.users.admin.AdminRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.event.UserEvent;
import ru.practicum.model.event.UserEventMapper;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventServiceImpl implements UserEventService {

    private final UserEventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final AdminRepository adminRepository;
    private final RequestsRepository requestsRepository;

    @Override
    public EventFullDto createEvent(NewEventDto newEventDto, long userId) {
        Optional<User> user = adminRepository.findById(userId);
        checkForUserExist(user);
        Optional<Category> category = categoryRepository.findById((long) newEventDto.getCategory());
        checkForCategoryExist(category);
        UserEvent userEvent = UserEventMapper.toUserEvent(newEventDto);
        if (newEventDto.getEventDate() != null ) {
            LocalDateTime timeToUpdate = LocalDateTime.parse(newEventDto.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(timeToUpdate.isBefore(LocalDateTime.now())) {
                throw new ValidationException("Дата уже наступила");
            }
            userEvent.setEventDate(timeToUpdate);
        }
        userEvent.setCategory(category.get());
        userEvent.setConfirmedRequests(0);
        userEvent.setCreatedOn(LocalDateTime.now());
        userEvent.setInitiator(user.get());
        userEvent.setState(State.PENDING);
        userEvent.setViews(5);
        userEvent = eventRepository.save(userEvent);
        log.info("Сохраненный ивент: {}", userEvent);
        return UserEventMapper.toEventDtoFromEvent(userEvent);
    }

    @Override
    public List<EventFullDto> getEventForCurrentUser(long userId, int from, int size) {
        Optional<User> user = adminRepository.findById(userId);
        checkForUserExist(user);
        return eventRepository.findByInitiator_Id(userId, PageRequest.of(from, size)).stream()
                .map(x -> UserEventMapper.toEventDtoFromEvent(x)).collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getRequests(long userId, long eventId) {
        adminRepository.findById(userId).orElseThrow(() -> {
            return new NotFoundException("Пользователь не найден");
        });
        eventRepository.findById(eventId).orElseThrow(() -> {
            return new NotFoundException("Событие не найдено");
        });
        return requestsRepository.findByEvent_id(eventId)
                .stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getCurrentEvent(long userId, long eventId) {
        Optional<User> user = adminRepository.findById(userId);
        checkForUserExist(user);
        try {
            return UserEventMapper.toEventDtoFromEvent(eventRepository.findById(eventId).get());
        } catch (NotFoundException e) {
            throw new NotFoundException("Событие не найдено");
        }
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequest(long userId, long eventId,
                                                             EventRequestStatusUpdateRequest request) {
        adminRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("Пользователь не найден");
        });
        UserEvent event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Событие не найдено");
        });
        if (userId != event.getInitiator().getId()) {
            throw new ValidationException("Вы не можете изменить статус приглашения, так как не являетесь " +
                    "организатором события");
        }

        List<Request> requests = requestsRepository
                .findByIdIn(request.getRequestIds());
        if (requests.size() == 1 && requests.get(0).getStatus().equals(Status.CONFIRMED)) {
            throw new ConflictException("Вы не можете принять уже принятые заявки");
        }
        EventRequestStatusUpdateResult updateRequestsStatus = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        if (request.getStatus().equals(Status.REJECTED)) {
            for (Request requestLoop : requests) {
                requestLoop.setStatus(Status.REJECTED);
                requestsRepository.save(requestLoop);
                rejectedRequests.add(RequestMapper.toDto(requestLoop));
            }
            updateRequestsStatus.setConfirmedRequests(new ArrayList<>());
            updateRequestsStatus.setRejectedRequests(rejectedRequests);
            eventRepository.minusConfirmedRequests(eventId, rejectedRequests.size());
        }
        if (!event.isRequestModeration() || request.getStatus().equals(Status.CONFIRMED)) {
            for (Request requestLoop : requests) {
                requestLoop.setStatus(Status.CONFIRMED);
                requestsRepository.save(requestLoop);
                confirmedRequests.add(RequestMapper.toDto(requestLoop));
            }
            updateRequestsStatus.setConfirmedRequests(confirmedRequests);
            updateRequestsStatus.setRejectedRequests(new ArrayList<>());
            eventRepository.plusConfirmedRequests(eventId, confirmedRequests.size());
        }
        int confirmed = eventRepository.getConfirmedReq(eventId);
        if(confirmed > event.getParticipantLimit()) {
            throw new ConflictException("нет мест");
        }
        return updateRequestsStatus;
    }

    @Override
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest update) {
        Optional<User> user = adminRepository.findById(userId);
        checkForUserExist(user);
        try {
            UserEvent event = eventRepository.findById(eventId).get();
            LocalDateTime dateEvent = event.getEventDate();
            if(event.getState().equals(State.PUBLISHED)) {
                throw new ConflictException("Событие уже опубликовано");
            }
            if ((event.getState().equals(State.PENDING) || event.getState().equals(State.CANCELED)) &&
                    event.getInitiator().getId() == userId && dateEvent.plusHours(2).isAfter(LocalDateTime.now())) {
                if(update.getStateAction() != null && update.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                    event.setState(State.PENDING);
                }
                if (update.getAnnotation() != null && !update.getAnnotation().isEmpty() && !update.getAnnotation().equals(event.getAnnotation())) {
                    event.setAnnotation(update.getAnnotation());
                }
                if (update.getCategory() > 0 && update.getCategory() != event.getCategory().getId()) {
                    Optional<Category> category = categoryRepository.findById((long) update.getCategory());
                    checkForCategoryExist(category);
                    event.setCategory(category.get());
                }
                if (update.getDescription() != null && !update.getDescription().isEmpty() && !update.getDescription().equals(event.getDescription())) {
                    event.setDescription(update.getDescription());
                }
                if (update.getStateAction() != null && update.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                    event.setState(State.CANCELED);
                }
                if (update.getEventDate() != null && !update.getEventDate().equals(event.getEventDate())) {
                    if (update.getEventDate() != null ) {
                        LocalDateTime timeToUpdate = LocalDateTime.parse(update.getEventDate(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        if(timeToUpdate.isBefore(LocalDateTime.now())) {
                            throw new ValidationException("Дата уже наступила");
                        }
                        event.setEventDate(timeToUpdate);
                    }
                }
                if (update.getLocation() != null && update.getLocation().getLat() != null && update.getLocation().getLat() != event.getLat() &&
                        update.getLocation().getLon() != null && update.getLocation().getLon() != event.getLon()) {
                    event.setLat(update.getLocation().getLat());
                    event.setLon(update.getLocation().getLon());
                }
                if (update.getParticipantLimit() > 0 && update.getParticipantLimit() != event.getParticipantLimit()) {
                    event.setParticipantLimit(update.getParticipantLimit());
                }
                if (update.isRequestModeration() != event.isRequestModeration()) {
                    event.setRequestModeration(update.isRequestModeration());
                }
                if (update.getTitle() != null && !update.getTitle().isEmpty() && !update.getTitle().equals(event.getTitle())) {
                    event.setTitle(update.getTitle());
                }
            }
            return UserEventMapper.toEventDtoFromEvent(event);
        } catch (NotFoundException e) {
            throw new NotFoundException("Событие не найдено");
        }
    }

    private void checkForUserExist(Optional<User> user) {
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователя не существует");
        }
    }

    public void checkForCategoryExist(Optional<Category> category) {
        if (category.isEmpty()) {
            throw new NotFoundException("Категория не найдена");
        }
    }
}

package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.State;
import ru.practicum.exception.AlreadyExist;
import ru.practicum.users.admin.AdminRepository;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.Status;
import ru.practicum.exception.NotFoundException;
import ru.practicum.event.users.UserEventRepository;
import ru.practicum.model.event.UserEvent;
import ru.practicum.model.user.User;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RequestsServiceImpl implements RequestsService {

    @Autowired
    private final RequestsRepository repository;
    @Autowired
    private final AdminRepository userRepository;
    @Autowired
    private final UserEventRepository eventRepository;

    @Override
    public ParticipationRequestDto create(long userId, long eventId) {
        Optional<User> user = userRepository.findById(userId);
        checkForUserExist(user);
        Optional<UserEvent> event = eventRepository.findById(eventId);
        checkForEventExist(event);
        if(!event.get().getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Статус неверный")
                    ;
        }
        Optional<Request> checkRequest = repository.findByRequesterAndEvent_Id(userId, eventId);
        if(!checkRequest.isEmpty()) {
            throw new AlreadyExist("Вы уже подали заявку на это мероприятие");
        }
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event.get());
        request.setRequester(userId);
        request.setStatus(Status.PENDING);
        if(!event.get().isRequestModeration()){
            request.setStatus(Status.CONFIRMED);
            eventRepository.plusConfirmedRequests(eventId, 1);
        }
        return RequestMapper.toDto(repository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getRequests(long userId) {
        Optional<User> user = userRepository.findById(userId);
        checkForUserExist(user);
        return repository.findByEvent_Initiator_IdNotAndRequester(userId, userId)
                .stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        Optional<User> user = userRepository.findById(userId);
        checkForUserExist(user);
        Optional<Request> request = repository.findById(requestId);
        checkForRequestExist(request);
        request.get().setStatus(Status.CANCELED);
        repository.updateStatus(Status.CANCELED, requestId);
        eventRepository.minusConfirmedRequests(request.get().getEvent().getId(), 1);
        return RequestMapper.toDto(request.get());
    }

    private void checkForUserExist(Optional<User> user) {
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователя не существует");
        }
    }

    private void checkForEventExist(Optional<UserEvent> event) {
        if (event.isEmpty()) {
            throw new NotFoundException("События не существует");
        }
    }

    private void checkForRequestExist(Optional<Request> request) {
        if(request.isEmpty()) {
            throw new NotFoundException("Заявка не найдена");
        }
    }
}

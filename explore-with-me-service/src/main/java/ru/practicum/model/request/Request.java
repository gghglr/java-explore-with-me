package ru.practicum.model.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.dto.request.Status;
import ru.practicum.model.event.UserEvent;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request")
@Getter
@Setter
@RequiredArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event")
    private UserEvent event;
    private long requester;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
}

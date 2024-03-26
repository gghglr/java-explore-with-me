package ru.practicum.request.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "hit")
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "app is null")
    private String app;
    @NotBlank(message = "uri is null")
    private String uri;
    private String ip;
    @Column(name = "timestamp_column")
    private LocalDateTime timestamp;

}

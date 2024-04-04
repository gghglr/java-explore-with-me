package ru.practicum.model.compilations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "compilations")
@RequiredArgsConstructor
public class Compilations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ElementCollection
    @CollectionTable(name = "compilation_events_id", joinColumns = @JoinColumn(name = "compilations_id"))
    @Column(name = "event_id")
    private List<Long> events;
    private boolean pinned;
    private String title;
}

package ru.practicum.compilations.compilations;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.compilations.Compilations;


public interface CompilationsRepository extends JpaRepository<Compilations, Long> {
}

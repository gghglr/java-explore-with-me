package ru.practicum.compilations.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.compilations.Compilations;

public interface AdminCompilationsRepository extends JpaRepository<Compilations, Long> {
}

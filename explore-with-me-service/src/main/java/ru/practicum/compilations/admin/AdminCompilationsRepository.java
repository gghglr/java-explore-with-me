package ru.practicum.compilations.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.compilations.Compilations;

import java.util.List;


public interface AdminCompilationsRepository extends JpaRepository<Compilations, Long> {

    List<Compilations> findByPinned(Boolean pinned, Pageable pageable);
}

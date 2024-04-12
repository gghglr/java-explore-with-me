package ru.practicum.users.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.model.user.User;

import java.util.List;

public interface AdminRepository extends JpaRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    List<User> findByIdIn(List<Long> ids, Pageable pageable);
}

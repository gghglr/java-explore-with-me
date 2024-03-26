package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.model.EndpointHit;
import ru.practicum.request.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.request.model.ViewStats(h.app, h.uri, count(h.ip)) FROM EndpointHit AS h WHERE" +
            " (timestamp_column BETWEEN ?1 AND ?2) AND h.uri in ?3 group by h.app, h.uri" +
            " order by count(h.ip) desc")
    List<ViewStats> getNoDistinct(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.request.model.ViewStats(h.app, h.uri, count(DISTINCT h.ip)) FROM EndpointHit AS h WHERE" +
            " (timestamp_column BETWEEN ?1 AND ?2) AND h.uri in ?3 group by h.app, h.uri" +
            " order by count(h.ip) DESC")
    List<ViewStats> getDistinct(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.request.model.ViewStats(h.app, h.uri, count(h.ip)) FROM EndpointHit AS h WHERE" +
            " (timestamp_column BETWEEN ?1 AND ?2) group by h.app, h.uri" +
            " order by count(h.ip) DESC")
    List<ViewStats> getAllWithNoDistinct(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.request.model.ViewStats(h.app, h.uri, count(h.ip)) FROM EndpointHit AS h WHERE" +
            " (timestamp_column BETWEEN ?1 AND ?2) group by h.app, h.uri" +
            " order by count(h.ip) DESC")
    List<ViewStats> getAllWithDistinct(LocalDateTime start, LocalDateTime end);
}

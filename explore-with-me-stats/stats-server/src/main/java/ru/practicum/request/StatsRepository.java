package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.request.model.EndpointHit;
import ru.practicum.request.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.request.model.ViewStats(h.app, h.uri, count(h.ip)) FROM EndpointHit AS h WHERE" +
            " (timestamp_column BETWEEN :start AND :end) AND h.uri in :uris group by h.app, h.uri" +
            " order by count(h.ip) desc")
    List<ViewStats> getNoDistinct(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                  @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.request.model.ViewStats(h.app, h.uri, count(DISTINCT h.ip)) FROM EndpointHit AS h WHERE" +
            " (timestamp_column BETWEEN :start AND :end) AND h.uri in :uris group by h.app, h.uri" +
            " order by count(h.ip) DESC")
    List<ViewStats> getDistinct(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.request.model.ViewStats(h.app, h.uri, count(h.ip)) FROM EndpointHit AS h WHERE" +
            " (timestamp_column BETWEEN :start AND :end) group by h.app, h.uri" +
            " order by count(h.ip) DESC")
    List<ViewStats> getAllWithNoDistinct(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.request.model.ViewStats(h.app, h.uri, count(DISTINCT h.ip)) FROM EndpointHit AS h WHERE" +
            " (timestamp_column BETWEEN :start AND :end) group by h.app, h.uri" +
            " order by count(h.ip) DESC")
    List<ViewStats> getAllWithDistinct(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}

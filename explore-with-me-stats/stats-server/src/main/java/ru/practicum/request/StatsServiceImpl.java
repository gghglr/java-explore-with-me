package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.request.model.HitViewMapper;
import ru.practicum.request.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public EndpointHitDto saveRequest(EndpointHitDto endpointHitDto) {

        return HitViewMapper.endpointHitDto(statsRepository.save(HitViewMapper.toEndpointHit(endpointHitDto)));
    }

    @Override
    public List<ViewStats> getRequests(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> stats;
        if (unique) {
            if (!uris.isEmpty()) {
                stats = statsRepository.getDistinct(start, end, uris);
            } else {
                stats = statsRepository.getAllWithDistinct(start, end);
            }
        } else {
            if (uris.isEmpty()) {
                stats = statsRepository.getAllWithNoDistinct(start, end);
            } else {
                stats = statsRepository.getNoDistinct(start, end, uris);
            }
        }
        return stats;
    }
}

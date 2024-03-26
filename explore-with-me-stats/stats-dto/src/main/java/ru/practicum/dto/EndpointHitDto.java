package ru.practicum.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EndpointHitDto {

    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}

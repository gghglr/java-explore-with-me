package ru.practicum.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class EndpointHitDto {

    @NotBlank(message = "app is null")
    private String app;
    @NotBlank(message = "uri is null")
    private String uri;
    @NotBlank(message = "ip is null")
    private String ip;
    @NotBlank(message = "time is null")
    private String timestamp;
}

package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;

import org.springframework.util.MultiValueMap;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class StatsClient {

    protected final WebClient webClient;

    private void makeAndDoGetRequest(String path) {
        webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private void makeAndDoPostRequest(String path, MultiValueMap<String, String> body) {
        webClient.post()
                .uri(path)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private void makeAndDoUpdateRequest(String path, MultiValueMap<String, String> body) {
        webClient.put()
                .uri(path)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private void makeAndDoDeleteRequest(String path) {
        webClient.delete()
                .uri(path)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

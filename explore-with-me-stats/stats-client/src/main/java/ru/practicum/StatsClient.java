package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;

import org.springframework.util.MultiValueMap;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ru.practicum.dto.EndpointHitDto;

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



    /*
    Для отправки POST запроса из одного микросервиса в другой с использованием WebFlux в Java, вы можете использовать WebClient.

Пример кода:

```java
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class PostRequestExample {

    public static void main(String[] args) {
        WebClient client = WebClient.create("http://другой_микросервис_url");

        client.post()
              .uri("/путь_к_api")
              .body(BodyInserters.fromValue("тело_запроса"))
              .exchange()
              .flatMap(response -> {
                  if (response.statusCode().is2xxSuccessful()) {
                      return response.bodyToMono(String.class);
                  } else {
                      return response.createException().flatMap(Mono::error);
                  }
              })
              .subscribe(System.out::println);
    }
}
```

Замените "другой_микросервис_url" на URL вашего другого микросервиса и "путь_к_api" на путь к API, к которому вы хотите отправить POST запрос. Также замените "тело_запроса" на данные, которые вы хотите отправить в теле запроса.

Этот код создает WebClient, который отправляет POST запрос по указанному URL и пути к API, передавая данные в теле запроса. Когда ответ получен, он выводит ответ в консоль.
     */
}

package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final NotFoundException e) {
        log.error("error = {}, httpStatus = {}", e.getMessage(), HttpStatus.NOT_FOUND);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotFound(final AlreadyExist e) {
        log.error("error = {}, httpStatus = {}", e.getMessage(), HttpStatus.NOT_FOUND);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotFound(final ValidationException e) {
        log.error("error = {}, httpStatus = {}", e.getMessage(), HttpStatus.NOT_FOUND);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleNotFound(final ConflictException e) {
        log.error("error = {}, httpStatus = {}", e.getMessage(), HttpStatus.NOT_FOUND);
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({IllegalStateException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(final Throwable e) {
        log.error("error = {}, httpStatus = {}", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return Map.of("error", e.getMessage());
    }
}

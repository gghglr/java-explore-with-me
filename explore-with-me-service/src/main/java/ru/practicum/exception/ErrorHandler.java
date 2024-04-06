package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({MissingRequestHeaderException.class, MethodArgumentNotValidException.class,
            NumberFormatException.class, ConstraintViolationException.class, ValidationException.class,
            MissingServletRequestParameterException.class
            })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(final Exception exception) {
        log.error("error = {}, httpStatus = {}", exception.getMessage(), HttpStatus.BAD_REQUEST);

        return new ApiError(
                Collections.emptyList(),
                exception.getMessage(),
                "Incorrectly made request.",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH::mm:ss"))
        );
    }

    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(final Exception exception) {
        log.error("error = {}, httpStatus = {}", exception.getMessage(), HttpStatus.CONFLICT);

        return new ApiError(
                Collections.emptyList(),
                exception.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH::mm:ss"))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final NotFoundException exception) {
        log.error("error = {}, httpStatus = {}", exception.getMessage(), HttpStatus.NOT_FOUND);

        return new ApiError(
                Collections.emptyList(),
                exception.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH::mm:ss"))
        );
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(final Throwable exception) {
        log.error("error = {}, httpStatus = {}", exception, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ApiError(
                Collections.emptyList(),
                exception.getMessage(),
                "The request was issued internal server error.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH::mm:ss"))
        );
    }

}

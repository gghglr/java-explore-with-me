package ru.practicum.dto.compilation;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class NewCompilationDto {

    private List<Long> events;
    private boolean pinned;
    @NotBlank(message = "Заголовок не должен быть пустым")
    @Size(min = 1, max = 50, message = "Заголовок должен быть больше 1 символа, но меньше 50")
    private String title;

}

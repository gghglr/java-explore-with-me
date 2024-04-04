package ru.practicum.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewUserRequest {

    @NotBlank(message = "emil can't be null")
    @Email(message = "email is invalid")
    @Size(min = 6, max = 254)
    private String email;
    @NotBlank(message = "name can't be null")
    @Size(min = 2, max = 250)
    private String name;
}

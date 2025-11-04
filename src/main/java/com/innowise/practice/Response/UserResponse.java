package com.innowise.practice.Response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserResponse implements Serializable {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birth_date;
    private String email;

}

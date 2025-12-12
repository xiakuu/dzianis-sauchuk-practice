package com.innowise.practice.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserRequest{
        @NotBlank
        private String name;
        @NotBlank
        private String surname;
        @Past
        @NotNull
        @DateTimeFormat
        private LocalDate birth_date;
        @NotBlank
        @Email
        private String email;
}

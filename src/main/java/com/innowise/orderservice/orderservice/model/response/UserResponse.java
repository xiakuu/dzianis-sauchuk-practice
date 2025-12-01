package com.innowise.orderservice.orderservice.model.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {
    private String name;
    private String surname;
    private LocalDate birth_date;
    private String email;


}

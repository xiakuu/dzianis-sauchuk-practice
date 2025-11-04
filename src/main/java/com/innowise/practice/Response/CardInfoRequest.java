package com.innowise.practice.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CardInfoRequest {
    @NotNull
    private Long userId;

    @NotNull
    @Min(1000000000000000L)
    @Max(9999999999999999L)
    private String number;

    @NotNull
    private String holder;

    @NotNull
    @Future
    private LocalDate expirationDate;
}

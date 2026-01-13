package com.payment_service.paymentservice.model.kafkaEvents.consumer;

import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderEvent {
    private Long id;
    private Long userId;
    private BigDecimal paymentAmonut;
}

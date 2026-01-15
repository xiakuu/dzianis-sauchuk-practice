package com.payment_service.paymentservice.model.kafkaEvents.producer;

import com.payment_service.paymentservice.model.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentEvent {
    private Long id;
    private DocumentStatus status;
}

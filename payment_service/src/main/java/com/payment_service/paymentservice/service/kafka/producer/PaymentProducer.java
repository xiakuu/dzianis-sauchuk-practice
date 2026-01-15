package com.payment_service.paymentservice.service.kafka.producer;

import com.payment_service.paymentservice.model.kafkaEvents.producer.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProducer {
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void sendPaymentStatus(PaymentEvent paymentEvent) {
        kafkaTemplate.send("create_payment", paymentEvent);
    }
}

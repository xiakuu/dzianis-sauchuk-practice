package com.payment_service.paymentservice.service.kafka.consumer;

import com.payment_service.paymentservice.model.kafkaEvents.consumer.OrderEvent;
import com.payment_service.paymentservice.model.kafkaEvents.producer.PaymentEvent;
import com.payment_service.paymentservice.model.request.DocumentRequest;
import com.payment_service.paymentservice.model.response.DocumentResponse;
import com.payment_service.paymentservice.service.DocumentService;
import com.payment_service.paymentservice.service.kafka.producer.PaymentProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final DocumentService documentService;
    private final PaymentProducer paymentProducer;

    @KafkaListener(topics = "create_order", groupId = "testgroup")
    public void handleCreateOrder(OrderEvent orderEvent) {
        System.out.println("handling order event: " + orderEvent);
        DocumentResponse documentResponse = documentService.createDocument(new DocumentRequest(orderEvent.getId(), orderEvent.getUserId(), orderEvent.getPaymentAmonut()));

        paymentProducer.sendPaymentStatus(new PaymentEvent(documentResponse.getOrderId(), documentResponse.getStatus()));

    }

}

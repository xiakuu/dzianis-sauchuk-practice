package com.payment_service.paymentservice.model.response;

import com.payment_service.paymentservice.model.DocumentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DocumentResponse {
    private String id;
    private Long orderId;
    private Long userId;
    private DocumentStatus status;
    private LocalDateTime timestamp;
    private BigDecimal paymentAmount;
}

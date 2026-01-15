package com.payment_service.paymentservice.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "documents")
public class DocumentModel {
    @Id
    private String id;
    private Long orderId;
    private Long userId;
    private DocumentStatus status;
    @CreatedDate
    private LocalDateTime timestamp;
    private BigDecimal paymentAmount;
}

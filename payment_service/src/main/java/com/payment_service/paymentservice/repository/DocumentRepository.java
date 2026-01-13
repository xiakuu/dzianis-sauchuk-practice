package com.payment_service.paymentservice.repository;

import com.payment_service.paymentservice.model.DocumentModel;
import com.payment_service.paymentservice.model.DocumentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentModel, Long> {
    List<DocumentModel> findByUserId(Long userId);
    List<DocumentModel> findByOrderId(Long orderId);
    List<DocumentModel> findByStatus(DocumentStatus status);
    List<DocumentModel> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

}

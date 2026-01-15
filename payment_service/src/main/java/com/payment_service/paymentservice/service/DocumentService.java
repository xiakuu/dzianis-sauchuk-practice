package com.payment_service.paymentservice.service;

import com.payment_service.paymentservice.externalRandomNumber.RandomNumberGenerator;
import com.payment_service.paymentservice.mapper.DocumentMapper;
import com.payment_service.paymentservice.model.DocumentModel;
import com.payment_service.paymentservice.model.DocumentStatus;
import com.payment_service.paymentservice.model.request.DocumentRequest;
import com.payment_service.paymentservice.model.response.DocumentResponse;
import com.payment_service.paymentservice.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final RandomNumberGenerator randomNumberGenerator;

    public DocumentResponse createDocument(DocumentRequest documentRequest) {
        System.out.println("received request kafka: " + documentRequest);
        DocumentModel documentModel = documentMapper.toDocument(documentRequest);
        if(randomNumberGenerator.getRandomNumber(10000) % 2 == 0){
            documentModel.setStatus(DocumentStatus.SUCCESS);
        } else {
            documentModel.setStatus(DocumentStatus.FAILED);
        }
        return documentMapper.toDocumentResponse(documentRepository.save(documentModel));
    }

    public List<DocumentResponse> getDocumentsByOrderId(Long orderId) {
        return documentRepository.findByOrderId(orderId).stream().map(documentMapper::toDocumentResponse).toList();
    }

    public List<DocumentResponse> getDocumentsByUserId(Long userId) {
        return documentRepository.findByUserId(userId).stream().map(documentMapper::toDocumentResponse).toList();
    }

    public List<DocumentResponse> getDocumentsByStatus(DocumentStatus status) {
        return documentRepository.findByStatus(status).stream().map(documentMapper::toDocumentResponse).toList();
    }

    public BigDecimal getTotalSumForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return documentRepository.findByTimestampBetween(startDate, endDate).stream().map(DocumentModel::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

package com.payment_service.paymentservice.unit;

import com.payment_service.paymentservice.externalRandomNumber.RandomNumberGenerator;
import com.payment_service.paymentservice.mapper.DocumentMapper;
import com.payment_service.paymentservice.model.DocumentModel;
import com.payment_service.paymentservice.model.DocumentStatus;
import com.payment_service.paymentservice.model.request.DocumentRequest;
import com.payment_service.paymentservice.model.response.DocumentResponse;
import com.payment_service.paymentservice.repository.DocumentRepository;
import com.payment_service.paymentservice.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceUnitTest {
    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentMapper documentMapper;

    @Mock
    private RandomNumberGenerator randomNumberGenerator;

    @InjectMocks
    private DocumentService documentService;

    @Test
    void createDocument_ShouldSetSuccessStatus_WhenRandomIsEven() {
        DocumentRequest request = new DocumentRequest(10L, 20L, BigDecimal.TEN);
        DocumentModel model = new DocumentModel();
        DocumentResponse expectedResponse = new DocumentResponse();

        when(documentMapper.toDocument(request)).thenReturn(model);
        when(randomNumberGenerator.getRandomNumber(10000)).thenReturn(2);
        when(documentRepository.save(any(DocumentModel.class))).thenReturn(model);
        when(documentMapper.toDocumentResponse(model)).thenReturn(expectedResponse);

        DocumentResponse actualResponse = documentService.createDocument(request);

        assertEquals(DocumentStatus.SUCCESS, model.getStatus());
        assertEquals(expectedResponse, actualResponse);
        verify(documentRepository).save(model);
    }

    @Test
    void createDocument_ShouldSetFailedStatus_WhenRandomIsOdd() {
        DocumentRequest request = new DocumentRequest(10L, 20L, BigDecimal.TEN);
        DocumentModel model = new DocumentModel();

        when(documentMapper.toDocument(request)).thenReturn(model);
        when(randomNumberGenerator.getRandomNumber(10000)).thenReturn(3);
        when(documentRepository.save(any(DocumentModel.class))).thenReturn(model);

        documentService.createDocument(request);

        assertEquals(DocumentStatus.FAILED, model.getStatus());
    }

    @Test
    void getDocumentsByOrderId_ShouldReturnList() {
        Long orderId = 1L;
        DocumentModel model = new DocumentModel();
        DocumentResponse response = new DocumentResponse();

        when(documentRepository.findByOrderId(orderId)).thenReturn(List.of(model));
        when(documentMapper.toDocumentResponse(model)).thenReturn(response);

        List<DocumentResponse> result = documentService.getDocumentsByOrderId(orderId);

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
    }

    @Test
    void getTotalSumForPeriod_ShouldCalculateCorrectly() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        DocumentModel doc1 = new DocumentModel();
        doc1.setPaymentAmount(new BigDecimal("100.50"));

        DocumentModel doc2 = new DocumentModel();
        doc2.setPaymentAmount(new BigDecimal("200.00"));

        when(documentRepository.findByTimestampBetween(start, end)).thenReturn(List.of(doc1, doc2));

        BigDecimal totalSum = documentService.getTotalSumForPeriod(start, end);

        assertEquals(new BigDecimal("300.50"), totalSum);
    }

    @Test
    void getDocumentsByStatus_ShouldReturnList() {
        DocumentStatus status = DocumentStatus.SUCCESS;
        DocumentModel model = new DocumentModel();
        when(documentRepository.findByStatus(status)).thenReturn(List.of(model));
        when(documentMapper.toDocumentResponse(model)).thenReturn(new DocumentResponse());

        List<DocumentResponse> result = documentService.getDocumentsByStatus(status);

        assertEquals(1, result.size());
        verify(documentRepository).findByStatus(status);
    }
}

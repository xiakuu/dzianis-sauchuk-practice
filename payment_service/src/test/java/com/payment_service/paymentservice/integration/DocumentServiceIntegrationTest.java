package com.payment_service.paymentservice.integration;

import com.payment_service.paymentservice.externalRandomNumber.RandomNumberGenerator;
import com.payment_service.paymentservice.model.DocumentModel;
import com.payment_service.paymentservice.model.DocumentStatus;
import com.payment_service.paymentservice.model.request.DocumentRequest;
import com.payment_service.paymentservice.model.response.DocumentResponse;
import com.payment_service.paymentservice.repository.DocumentRepository;
import com.payment_service.paymentservice.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
public class DocumentServiceIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:8.2.3"));

    @DynamicPropertySource
    static void mongoDBProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb", mongoDBContainer::getReplicaSetUrl);
    }

    @MockitoBean
    private RandomNumberGenerator randomNumberGenerator;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;

    @BeforeEach
    void cleanUp() {
        documentRepository.deleteAll();
    }

    @Test
    void createDocument_ShouldSaveToActualMongo() {
        DocumentRequest request = new DocumentRequest(100L, 50L, BigDecimal.valueOf(1500.00));

        when(randomNumberGenerator.getRandomNumber(10000)).thenReturn(2);

        DocumentResponse response = documentService.createDocument(request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(DocumentStatus.SUCCESS);

        List<DocumentModel> allInDb = documentRepository.findAll();
        assertThat(allInDb).hasSize(1);
        assertThat(allInDb.get(0).getOrderId()).isEqualTo(100L);
    }

    @Test
    void getTotalSumForPeriod_ShouldCalculateCorrectlyFromDb() {
        LocalDateTime now = LocalDateTime.now();

        DocumentModel doc1 = new DocumentModel();
        doc1.setPaymentAmount(new BigDecimal("100.00"));
        doc1.setTimestamp(now.minusHours(1));

        DocumentModel doc2 = new DocumentModel();
        doc2.setPaymentAmount(new BigDecimal("250.50"));
        doc2.setTimestamp(now.minusMinutes(30));

        DocumentModel oldDoc = new DocumentModel();
        oldDoc.setPaymentAmount(new BigDecimal("500.00"));
        oldDoc.setTimestamp(now.minusDays(5));

        documentRepository.saveAll(List.of(doc1, doc2, oldDoc));

        BigDecimal total = documentService.getTotalSumForPeriod(now.minusDays(1), now.plusHours(1));

        assertThat(total).isEqualByComparingTo("350.50");
    }

    @Test
    void getDocumentsByOrderId_ShouldReturnDataFromDb() {
        DocumentModel doc = new DocumentModel();
        doc.setOrderId(777L);
        doc.setStatus(DocumentStatus.SUCCESS);
        documentRepository.save(doc);

        List<DocumentResponse> results = documentService.getDocumentsByOrderId(777L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getOrderId()).isEqualTo(777L);
    }
}



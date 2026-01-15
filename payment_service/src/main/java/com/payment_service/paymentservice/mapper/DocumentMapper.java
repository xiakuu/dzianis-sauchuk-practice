package com.payment_service.paymentservice.mapper;

import com.payment_service.paymentservice.model.DocumentModel;
import com.payment_service.paymentservice.model.request.DocumentRequest;
import com.payment_service.paymentservice.model.response.DocumentResponse;
import org.mapstruct.*;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumentMapper {
    DocumentModel toDocument(DocumentRequest documentRequest);
    DocumentResponse toDocumentResponse(DocumentModel documentModel);

}

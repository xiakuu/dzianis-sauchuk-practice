package com.innowise.practice.Mapper;

import com.innowise.practice.Response.CardInfoRequest;
import com.innowise.practice.Response.CardInfoResponse;
import com.innowise.practice.DBSchema.CardInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardInfoMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "expiration_date", target = "expirationDate")
    CardInfoResponse toCardInfoResponse(CardInfo cardInfo);
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "expirationDate", target = "expiration_date")
    CardInfo toCardInfo(CardInfoRequest cardInfoRequest);
}

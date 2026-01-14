package com.innowise.practice.Unit;

import com.innowise.practice.Response.CardInfoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.innowise.practice.CustomExceptions.DuplicateException;
import com.innowise.practice.CustomExceptions.ResourceNotFoundException;
import com.innowise.practice.Repository.CardInfoRepository;
import com.innowise.practice.Mapper.CardInfoMapper;
import com.innowise.practice.Response.CardInfoResponse;
import com.innowise.practice.Response.UserResponse;
import com.innowise.practice.Service.CardInfoService;
import com.innowise.practice.DBSchema.CardInfo;
import com.innowise.practice.DBSchema.Users;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardInfoServiceTest {
    @Mock
    CardInfoRepository cardInfoRepository;

    @Mock
    CardInfoMapper cardInfoMapper;

    @InjectMocks
    CardInfoService cardInfoService;

    private Users user;
    private CardInfo card;
    private CardInfoResponse cardInfoResponse;
    private UserResponse userResponse;
    private CardInfoRequest cardInfoRequest;
    private Long userId;
    private Long cardId;


    @BeforeEach
    void setUp() {
        userId = 10L;
        user = new Users();
        user.setId(userId);
        user.setBirth_date(LocalDate.of(12, 3, 23));
        user.setSurname("testsurname");
        user.setEmail("testemail@test.com");

        userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setBirth_date(user.getBirth_date());
        userResponse.setSurname(user.getSurname());
        userResponse.setEmail(user.getEmail());



        cardId = 20L;
        card = new CardInfo();
        card.setId(cardId);
        card.setHolder("testholder");
        card.setNumber("1929394959697989");
        card.setExpiration_date(LocalDate.of(2029, 12, 12));

        cardInfoResponse = new CardInfoResponse();
        cardInfoResponse.setId(card.getId());
        cardInfoResponse.setHolder(card.getHolder());
        cardInfoResponse.setNumber(card.getNumber());
        cardInfoResponse.setExpirationDate(card.getExpiration_date());

    }

    @Test
    void getAllCardInfos() {
        CardInfo cardInfo1 = new CardInfo();
        CardInfo cardInfo2 = new CardInfo();
//        CardInfoResponse cardInfoResponse1 = new CardInfoResponse();
//        CardInfoResponse cardInfoResponse2 = new CardInfoResponse();
//        List<CardInfoResponse> cardInfoResponseList = new ArrayList<>(List.of(cardInfoResponse, cardInfoResponse1, cardInfoResponse2));
        List<CardInfo> cardInfoList = new ArrayList<>(List.of(card, cardInfo1, cardInfo2));


        Page<CardInfo> cardInfoPage = new PageImpl<>(cardInfoList);
        when(cardInfoRepository.findAll(PageRequest.of(0,5))).thenReturn(cardInfoPage);
        when(cardInfoMapper.toCardInfoResponse(any(CardInfo.class))).thenReturn(cardInfoResponse);
        List<CardInfoResponse> result = cardInfoService.getAllCards(0,5);

        assertNotNull(result);
        assertEquals(result.size(), cardInfoList.size());
    }

    @Test
    void getCardInfoById() {
        when(cardInfoRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardInfoMapper.toCardInfoResponse(card)).thenReturn(cardInfoResponse);
        CardInfoResponse result = cardInfoService.getCard(cardId);

        assertEquals(cardInfoResponse.getId(), result.getId());
    }

    @Test
    void getCardInfoById_NotFound() {
        when(cardInfoRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cardInfoService.getCard(cardId));
    }

    @Test
    void createCard() {
        when(cardInfoRepository.save(card)).thenReturn(card);
        when(cardInfoMapper.toCardInfoResponse(card)).thenReturn(cardInfoResponse);
        when(cardInfoMapper.toCardInfo(cardInfoRequest)).thenReturn(card);
        CardInfoResponse result = cardInfoService.createCard(cardInfoRequest);

        assertEquals(card.getId(), result.getId());
    }

    @Test
    void deleteCard() {
        when(cardInfoRepository.existsById(cardId)).thenReturn(true);

        assertDoesNotThrow(() -> cardInfoService.deleteCard(cardId));
    }

    @Test
    void deleteCard_NotFound() {
        when(cardInfoRepository.existsById(cardId)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> cardInfoService.deleteCard(cardId));
    }
}
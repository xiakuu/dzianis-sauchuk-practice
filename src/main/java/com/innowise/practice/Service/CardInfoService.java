package com.innowise.practice.Service;

import com.innowise.practice.CustomExceptions.DuplicateException;
import com.innowise.practice.CustomExceptions.ResourceNotFoundException;
import com.innowise.practice.Repository.CardInfoRepository;
import com.innowise.practice.Mapper.CardInfoMapper;
import com.innowise.practice.Response.CardInfoRequest;
import com.innowise.practice.Response.CardInfoResponse;
import com.innowise.practice.DBSchema.CardInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CardInfoService {

    private final CardInfoRepository cardInfoRepository;
    private final CardInfoMapper cardInfoMapper;


    @CachePut(value = "cards", key = "#result.id")
    public CardInfoResponse createCard (CardInfoRequest cardInfoRequest) {
        CardInfo savedCardInfo = cardInfoRepository.save(cardInfoMapper.toCardInfo(cardInfoRequest));
        System.out.println(savedCardInfo.getId());
        return cardInfoMapper.toCardInfoResponse(savedCardInfo);
    }
    @Cacheable(value = "cards", key = "#id")
    public CardInfoResponse getCard (Long id) {
        return cardInfoRepository.findById(id).map(cardInfoMapper::toCardInfoResponse).orElseThrow(() -> new ResourceNotFoundException("Card with id " + id + " not Found"));
    }

    public List<CardInfoResponse> getAllCards(int page, int size) {
        return cardInfoRepository.findAll(PageRequest.of(page, size)).stream().map(cardInfoMapper::toCardInfoResponse).collect(Collectors.toList());
    }

    @CacheEvict(value = "cards", key = "#id")
    @Transactional
    public void deleteCard (Long id) {
        if(!cardInfoRepository.existsById(id)){
            throw new ResourceNotFoundException("Card with id " + id + " not Found");
        }
        cardInfoRepository.deleteById(id);
    }
}

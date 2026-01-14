package com.innowise.practice.Controller;


import com.innowise.practice.Response.CardInfoRequest;
import com.innowise.practice.Response.CardInfoResponse;
import com.innowise.practice.Service.CardInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {
    private final CardInfoService cardInfoService;


    @GetMapping
    public ResponseEntity<List<CardInfoResponse>> getAllCards(@RequestParam int page, @RequestParam int size) {
        List<CardInfoResponse> cardInfoList = cardInfoService.getAllCards(page, size);
        return ResponseEntity.ok(cardInfoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardInfoResponse> getCardById(@PathVariable Long id) {
        CardInfoResponse cardInfoResponse = cardInfoService.getCard(id);
        return ResponseEntity.ok(cardInfoResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<CardInfoResponse> createCard(@Validated @RequestBody CardInfoRequest cardInfoRequest) {
        CardInfoResponse cardInfoResponseCreated = cardInfoService.createCard(cardInfoRequest);
        return ResponseEntity.ok(cardInfoResponseCreated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardInfoService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }


}

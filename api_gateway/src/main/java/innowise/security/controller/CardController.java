package innowise.security.controller;

import innowise.security.domain.model.CardInfo;
import innowise.security.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;


    @GetMapping("/admin/getall")
    public ResponseEntity<List<CardInfo>> getCardInfo() {
        List<CardInfo> cardInfos = cardService.getAllCards();
        return ResponseEntity.ok(cardInfos);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CardInfo> getCardInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cardService.getCard(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CardInfo> createCard(@RequestBody CardInfo cardInfo) {
        cardService.createCard(cardInfo);
        return ResponseEntity.ok(cardInfo);
    }
}

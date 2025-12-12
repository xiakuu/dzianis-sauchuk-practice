package innowise.security.service;

import innowise.security.domain.model.CardInfo;
import innowise.security.repository.CardsRepository;
import innowise.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CardService {
    private final CardsRepository cardsRepository;


    public CardInfo createCard(CardInfo cardInfo) {
        return cardsRepository.save(cardInfo);
    }

    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCardOwner(#cardId)")
    public CardInfo getCard(Long cardId) {
        return cardsRepository.findById(cardId).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCardOwner(#cardId)")
    public CardInfo updateCard(Long cardId, CardInfo cardInfo) {
        CardInfo card = getCard(cardId);
        card.setUser(cardInfo.getUser());
        card.setNumber( cardInfo.getNumber());
        card.setExpirationDate(cardInfo.getExpirationDate());
        card.setHolder(cardInfo.getHolder());
        return cardsRepository.save(card);

    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CardInfo> getAllCards() {
        return cardsRepository.findAll();
    }
}

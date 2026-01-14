package innowise.security.repository;

import innowise.security.domain.model.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardsRepository extends JpaRepository<CardInfo, Long> {
}

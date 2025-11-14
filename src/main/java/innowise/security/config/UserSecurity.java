package innowise.security.config;

import innowise.security.repository.CardsRepository;
import innowise.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurity {
    private final UserRepository userRepository;
    private final CardsRepository cardsRepository;

    public boolean isUserOwner(Long userId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || !auth.isAuthenticated()) return false;
        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) return true;
        String login = auth.getName();
        return userRepository.findById(userId)
                .map(us -> us.getUsername().equals(login))
                .orElse(false);
    }

    public boolean isCardOwner(Long cardId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || !auth.isAuthenticated()) return false;
        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) return true;
        String login = auth.getName();
        return cardsRepository.findById(cardId)
                .map(cardInfo -> cardInfo.getUser() != null && login.equals(cardInfo.getUser().getUsername()))
                .orElse(false);
    }
}

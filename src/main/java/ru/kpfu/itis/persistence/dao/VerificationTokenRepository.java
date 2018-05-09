package ru.kpfu.itis.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.persistence.model.User;
import ru.kpfu.itis.persistence.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}

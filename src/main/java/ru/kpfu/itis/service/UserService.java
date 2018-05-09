package ru.kpfu.itis.service;

import ru.kpfu.itis.persistence.model.User;
import ru.kpfu.itis.persistence.model.VerificationToken;
import ru.kpfu.itis.web.dto.UserDto;
import ru.kpfu.itis.web.error.UserAlreadyExistException;

public interface UserService {
    User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException;

    User getUser(String verificationToken);

    User getUserByEmail(String email);

    void saveRegisteredUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String verificationToken);
}

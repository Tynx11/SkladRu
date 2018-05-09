package ru.kpfu.itis.web.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import ru.kpfu.itis.validation.PasswordMatches;
import ru.kpfu.itis.validation.ValidEmail;
import ru.kpfu.itis.validation.ValidPassword;

@PasswordMatches
public class UserDto {

    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";
    private static final String EMAIL_MESSAGE = "{email.message}";
    private static final String EMAIL_EXISTS_MESSAGE = "{email-exists.message}";

//    @EmailExists(message = UserDto.EMAIL_EXISTS_MESSAGE)
    @NotBlank(message = UserDto.NOT_BLANK_MESSAGE)
    @ValidEmail(message = UserDto.EMAIL_MESSAGE)
    private String email;

    @NotBlank(message = UserDto.NOT_BLANK_MESSAGE)
    @ValidPassword
    private String password;

    @NotBlank
    private String matchingPassword;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

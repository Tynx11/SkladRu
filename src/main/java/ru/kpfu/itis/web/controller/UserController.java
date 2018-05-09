package ru.kpfu.itis.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.persistence.dao.UserRepository;
import ru.kpfu.itis.persistence.model.User;
import ru.kpfu.itis.persistence.model.VerificationToken;
import ru.kpfu.itis.registration.OnRegistrationCompleteEvent;
import ru.kpfu.itis.service.UserService;
import ru.kpfu.itis.support.web.Ajax;
import ru.kpfu.itis.support.web.MessageHelper;
import ru.kpfu.itis.web.dto.UserDto;
import ru.kpfu.itis.web.error.UserAlreadyExistException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Calendar;
import java.util.Locale;

@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String SIGNUP_VIEW_NAME = "signup/signup";

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("signin")
    public String signin() {
        return "signin/signin";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
        model.addAttribute("signupForm", new UserDto());
        if (Ajax.isAjaxRequest(requestedWith)) {
            return SIGNUP_VIEW_NAME.concat(" :: signupForm");
        }
        return SIGNUP_VIEW_NAME;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView registerUserAccount(@ModelAttribute("signupForm") @Valid UserDto userDto, BindingResult result,
                                            WebRequest request, Errors errors) {
        if (result.hasErrors()) {
            return new ModelAndView(SIGNUP_VIEW_NAME, "signupForm", userDto);
        }

        User registered = null;
        try {
            registered = userService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistException e) {
            result.rejectValue("email", null, e.getMessage());
            return new ModelAndView(SIGNUP_VIEW_NAME);
        }

        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView(SIGNUP_VIEW_NAME, "signupForm", userDto);
        }
        return new ModelAndView("home/about", "user", userDto);
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(WebRequest request, RedirectAttributes ra, @RequestParam("token") String token) {
        Locale locale = request.getLocale();
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
//            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
            MessageHelper.addErrorAttribute(ra, "auth.message.invalidToken");
            //TODO: badUser error page
            return "redirect:/generalError";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            MessageHelper.addErrorAttribute(ra, "auth.message.expired");
            return "redirect:/generalError";
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        return "redirect:/signin";
//      return "redirect:/signin.html?lang=" + request.getLocale().getLanguage();
    }

    @GetMapping("account/current")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @ResponseBody
    public ru.kpfu.itis.persistence.model.User currentAccount(Principal principal) {
        Assert.notNull(principal);
        return userRepository.findOneByEmail(principal.getName());
    }

    @GetMapping("account/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    @ResponseBody
    public ru.kpfu.itis.persistence.model.User account(@PathVariable("id") Long id) {
        return userRepository.findOne(id);
    }
}

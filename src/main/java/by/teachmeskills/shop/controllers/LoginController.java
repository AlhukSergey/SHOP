package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import static by.teachmeskills.shop.enums.ShopConstants.USER;

@RestController
@SessionAttributes({USER})
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView openLoginPage() {
        return new ModelAndView(PagesPathEnum.LOGIN_PAGE.getPath());
    }

    @GetMapping("/registration")
    public ModelAndView openRegistrationPage() {
        return new ModelAndView(PagesPathEnum.REGISTRATION_PAGE.getPath());
    }

    @PostMapping
    public ModelAndView login(@ModelAttribute(USER) User user) {
        return userService.authenticate(user);
    }

    @ModelAttribute(USER)
    public User setUpUserForm() {
        return new User();
    }
}

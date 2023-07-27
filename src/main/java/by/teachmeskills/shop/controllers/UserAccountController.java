package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static by.teachmeskills.shop.enums.ShopConstants.USER;

@RestController
@SessionAttributes({USER})
@RequestMapping("/account")
public class UserAccountController {
    private final UserService userService;

    public UserAccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView openAccountPage(User user) {
        return userService.generateAccountPage(user);
    }

    @PostMapping
    public ModelAndView updateUserData(User user, @RequestParam Map<String, String> allParams) {
        return userService.updateData(user, allParams);
    }
}

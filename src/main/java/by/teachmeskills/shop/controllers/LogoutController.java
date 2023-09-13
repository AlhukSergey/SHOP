package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.enums.PagesPathEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/logout")
public class LogoutController {
    @GetMapping
    public ModelAndView logOut() {
        return new ModelAndView(PagesPathEnum.HOME_PAGE.getPath());
    }
}

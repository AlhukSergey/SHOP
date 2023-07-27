package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.enums.PagesPathEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
@RestController
@RequestMapping("/myPage")
public class MyPageController {
    @GetMapping
    public ModelAndView openMyPage() {
        return new ModelAndView(PagesPathEnum.MY_PAGE.getPath());
    }
}

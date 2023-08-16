package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.services.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/home")
public class HomeController {
    private final CategoryService categoryService;

    public HomeController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ModelAndView openHomePage() throws EntityNotFoundException {
        return categoryService.getCategories();
    }

    @GetMapping("/{page}")
    public ModelAndView changePage(@PathVariable(value = "page") int currentPage) throws EntityNotFoundException {
        return categoryService.getPaginatedCategories(currentPage);
    }
}

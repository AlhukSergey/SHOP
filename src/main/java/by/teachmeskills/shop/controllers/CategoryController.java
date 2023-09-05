package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import by.teachmeskills.shop.services.CategoryService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ModelAndView openCategoryPage(@PathVariable int id) throws EntityNotFoundException {
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/csv/import")
    public ModelAndView uploadCategoriesFromFile(@RequestParam("file") MultipartFile file) {
        return categoryService.saveCategoriesFromFile(file);
    }

    @GetMapping("/csv/export")
    public void uploadCategoriesFromBD(HttpServletResponse response) throws ExportToFIleException {
        categoryService.saveCategoriesFromBD(response);
    }
}

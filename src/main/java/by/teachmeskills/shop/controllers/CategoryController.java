package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.enums.ShopConstants;
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
    public ModelAndView openCategoryPage(@PathVariable Integer id,
                                         @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                         @RequestParam(required = false, defaultValue = "" + ShopConstants.PAGE_SIZE) Integer pageSize) throws EntityNotFoundException {
        return categoryService.getCategoryById(id, pageNumber, pageSize);
    }

    @PostMapping("/csv/import")
    public ModelAndView importCategoriesFromCsv(@RequestParam("file") MultipartFile file,
                                                @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                @RequestParam(required = false, defaultValue = "" + ShopConstants.PAGE_SIZE) Integer pageSize) throws EntityNotFoundException {
        return categoryService.importCategoriesFromCsv(pageNumber, pageSize, file);
    }

    @GetMapping("/csv/export")
    public void exportCategoriesToCsv(HttpServletResponse response) throws ExportToFIleException {
        categoryService.exportCategoriesToCsv(response);
    }
}

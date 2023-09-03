package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.csv.CategoryCsv;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import by.teachmeskills.shop.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    @PostMapping("/toBD")
    public ResponseEntity<List<CategoryCsv>> uploadCategoriesFromFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(categoryService.saveCategoriesFromFile(file), HttpStatus.CREATED);
    }

    @GetMapping("/toFile/{fileName}")
    public ResponseEntity<String> uploadCategoriesFromBD(@PathVariable String fileName) throws ExportToFIleException {
        return new ResponseEntity<>(categoryService.saveCategoriesFromBD(fileName), HttpStatus.CREATED);
    }
}

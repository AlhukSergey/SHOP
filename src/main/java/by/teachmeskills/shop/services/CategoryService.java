package by.teachmeskills.shop.services;

import by.teachmeskills.shop.csv.CategoryCsv;
import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface CategoryService extends BaseService<Category> {
    ModelAndView getCategoryById(int id) throws EntityNotFoundException;

    ModelAndView getCategories() throws EntityNotFoundException;

    ModelAndView getPaginatedCategories(int currentPage) throws EntityNotFoundException;

    List<CategoryCsv> saveCategoriesFromFile(MultipartFile file);

    String saveCategoriesFromBD(String fileName) throws ExportToFIleException;
}

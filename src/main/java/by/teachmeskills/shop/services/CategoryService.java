package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

public interface CategoryService extends BaseService<Category> {
    ModelAndView getCategoryById(int id) throws EntityNotFoundException;

    ModelAndView getCategories() throws EntityNotFoundException;

    ModelAndView getPaginatedCategories(int currentPage) throws EntityNotFoundException;

    ModelAndView saveCategoriesFromFile(MultipartFile file);

    void saveCategoriesFromBD(HttpServletResponse response) throws ExportToFIleException;
}

package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

public interface CategoryService {
    Category create(Category entity);
    ModelAndView getCategoryById(int id, int pageNumber, int pageSize) throws EntityNotFoundException;

    ModelAndView getAllCategories(int pageNumber, int pageSize) throws EntityNotFoundException;

    ModelAndView importCategoriesFromCsv(int pageNumber, int pageSize, MultipartFile file) throws EntityNotFoundException;

    void exportCategoriesToCsv(HttpServletResponse response) throws ExportToFIleException;
}

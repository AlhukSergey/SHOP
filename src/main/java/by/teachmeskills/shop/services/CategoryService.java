package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.Category;
import org.springframework.web.servlet.ModelAndView;

public interface CategoryService extends BaseService<Category> {
    ModelAndView getCategoryById(int id);
    ModelAndView getCategories();
}

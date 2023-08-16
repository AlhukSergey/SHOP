package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.Product;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface ProductService extends BaseService<Product>{
    Product getProductById(int id);

    List<Product> getProductsByCategoryId(int categoryId) throws EntityNotFoundException;

    ModelAndView getProductsBySearchParameter(String parameter) throws EntityNotFoundException;

    ModelAndView getProductData(int id) throws EntityNotFoundException;
}

package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.Product;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface ProductService extends BaseService<Product>{
    Product getProductById(int id);

    List<Product> getProductsByCategoryId(int categoryId);

    ModelAndView getProductsBySearchParameter(String parameter);

    ModelAndView getProductData(int id);
}

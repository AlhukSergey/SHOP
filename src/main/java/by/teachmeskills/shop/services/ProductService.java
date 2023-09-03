package by.teachmeskills.shop.services;

import by.teachmeskills.shop.csv.ProductCsv;
import by.teachmeskills.shop.domain.Product;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface ProductService extends BaseService<Product>{
    Product getProductById(int id);

    List<Product> getProductsByCategoryId(int categoryId) throws EntityNotFoundException;

    ModelAndView getProductsBySearchParameter(String parameter) throws EntityNotFoundException;

    ModelAndView getProductData(int id) throws EntityNotFoundException;

    List<ProductCsv> saveProductsFromFile(MultipartFile file) throws Exception;
    String saveProductsFromBD(String fileName) throws ExportToFIleException;
}

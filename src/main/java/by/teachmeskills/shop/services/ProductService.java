package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.Product;
import by.teachmeskills.shop.domain.Search;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface ProductService {
    Product create(Product entity);
    void getProductsByCategoryId(int categoryId) throws EntityNotFoundException;
    List<Product> getProductsByCategoryId(int categoryId, Pageable pageable) throws EntityNotFoundException;

    ModelAndView getProductsBySearchParameters(Search search, int pageNumber, int pageSize);

    ModelAndView getProductData(int id);

    ModelAndView importProductsFromCsv(int pageNumber, int pageSize, MultipartFile file) throws Exception;

    void exportProductsToCsv(HttpServletResponse response, int categoryId) throws ExportToFIleException;
}

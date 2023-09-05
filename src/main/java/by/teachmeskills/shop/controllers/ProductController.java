package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import by.teachmeskills.shop.services.ProductService;
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
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ModelAndView openProductPage(@PathVariable int id) throws EntityNotFoundException {
        return productService.getProductData(id);
    }

    @PostMapping("/csv/import")
    public ModelAndView uploadCategoriesFromFile(@RequestParam("file") MultipartFile file) throws Exception {
        return productService.saveProductsFromFile(file);
    }

    @GetMapping("/csv/export/{categoryId}")
    public void uploadProductsFromBD(HttpServletResponse response, @PathVariable int categoryId) throws ExportToFIleException {
        productService.saveProductsFromBD(response, categoryId);
    }
}

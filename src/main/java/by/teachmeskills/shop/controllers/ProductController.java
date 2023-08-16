package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}

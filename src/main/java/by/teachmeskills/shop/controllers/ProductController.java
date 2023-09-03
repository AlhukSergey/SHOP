package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.csv.ProductCsv;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import by.teachmeskills.shop.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    @PostMapping("/toBD")
    public ResponseEntity<List<ProductCsv>> uploadCategoriesFromFile(@RequestParam("file") MultipartFile file) throws Exception {
        return new ResponseEntity<>(productService.saveProductsFromFile(file), HttpStatus.CREATED);
    }

    @GetMapping("/toFile/{fileName}")
    public ResponseEntity<String> uploadProductsFromBD(@PathVariable String fileName) throws ExportToFIleException {
        return new ResponseEntity<>(productService.saveProductsFromBD(fileName), HttpStatus.CREATED);
    }
}

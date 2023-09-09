package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.enums.ShopConstants;
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
    public ModelAndView openProductPage(@PathVariable int id) {
        return productService.getProductData(id);
    }

    @PostMapping("/csv/import")
    public ModelAndView importProductsFromCsv(@RequestParam("file") MultipartFile file,
                                              @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                              @RequestParam(required = false, defaultValue = "" + ShopConstants.PAGE_SIZE) Integer pageSize) throws Exception {
        return productService.importProductsFromCsv(pageNumber, pageSize, file);
    }

    @GetMapping("/csv/export/{categoryId}")
    public void exportProductsToCsv(HttpServletResponse response, @PathVariable int categoryId) throws ExportToFIleException {
        productService.exportProductsToCsv(response, categoryId);
    }
}

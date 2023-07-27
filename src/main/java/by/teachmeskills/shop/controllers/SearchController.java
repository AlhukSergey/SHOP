package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final ProductService productService;

    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ModelAndView openSearchPage() {
        return new ModelAndView(PagesPathEnum.SEARCH_PAGE.getPath());
    }

    @PostMapping("/findProducts")
    public ModelAndView search(@RequestParam("search_param") String searchParameter) {
        return productService.getProductsBySearchParameter(searchParameter);
    }
}

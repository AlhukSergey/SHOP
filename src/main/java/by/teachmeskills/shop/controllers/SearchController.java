package by.teachmeskills.shop.controllers;

import by.teachmeskills.shop.domain.Search;
import by.teachmeskills.shop.enums.ShopConstants;
import by.teachmeskills.shop.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import static by.teachmeskills.shop.enums.ShopConstants.SEARCH;

@RestController
@RequestMapping("/search")
@SessionAttributes({SEARCH})
public class SearchController {
    private final ProductService productService;

    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ModelAndView openSearchPage(@SessionAttribute(name = SEARCH, required = false) Search search,
                                       @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                       @RequestParam(required = false, defaultValue = "" + ShopConstants.PAGE_SIZE) Integer pageSize) {
        return productService.getProductsBySearchParameters(search, pageNumber, pageSize);
    }

    @PostMapping
    public ModelAndView search(@ModelAttribute(SEARCH) Search search,
                               @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                               @RequestParam(required = false, defaultValue = "" + ShopConstants.PAGE_SIZE) Integer pageSize) {
        return productService.getProductsBySearchParameters(search, pageNumber, pageSize);
    }

    @ModelAttribute(SEARCH)
    public Search setUpSearch() {
        return new Search();
    }
}

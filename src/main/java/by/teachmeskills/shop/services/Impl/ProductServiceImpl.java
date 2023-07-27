package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.domain.Product;
import by.teachmeskills.shop.enums.InfoEnum;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.enums.RequestParamsEnum;
import by.teachmeskills.shop.repositories.ProductRepository;
import by.teachmeskills.shop.services.ImageService;
import by.teachmeskills.shop.services.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static by.teachmeskills.shop.enums.RequestParamsEnum.IMAGES;
import static by.teachmeskills.shop.enums.RequestParamsEnum.PRODUCTS;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ImageService imageService;

    public ProductServiceImpl(ProductRepository productRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.imageService = imageService;
    }

    @Override
    public Product create(Product entity) {
        return productRepository.create(entity);
    }

    @Override
    public List<Product> read() {
        return productRepository.read();
    }

    @Override
    public Product update(Product entity) {
        return productRepository.update(entity);
    }

    @Override
    public void delete(int id) {
        productRepository.delete(id);
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getProductsByCategoryId(int categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public ModelAndView getProductsBySearchParameter(String parameter) {
        ModelMap model = new ModelMap();
        List<Product> products = productRepository.findBySearchParameter(parameter);

        if (!products.isEmpty()) {
            List<List<Image>> images = new ArrayList<>();

            for (Product product : products) {
                images.add(imageService.getImagesByProductId(product.getId()));
            }

            model.addAttribute(PRODUCTS.getValue(), products);
            model.addAttribute(IMAGES.getValue(), images.stream().flatMap(Collection::stream).collect(Collectors.toList()));

            return new ModelAndView(PagesPathEnum.SEARCH_PAGE.getPath(), model);
        }

        model.addAttribute(RequestParamsEnum.INFO.getValue(), InfoEnum.PRODUCTS_NOT_FOUND_INFO.getInfo());
        return new ModelAndView(PagesPathEnum.SEARCH_PAGE.getPath(), model);
    }

    @Override
    public ModelAndView getProductData(int id) {
        ModelMap model = new ModelMap();
        Product product = productRepository.findById(id);
        if (Optional.ofNullable(product).isPresent()) {
            model.addAttribute(RequestParamsEnum.PRODUCT.getValue(), product);
            model.addAttribute(IMAGES.getValue(), imageService.getImagesByProductId(id));
        }
        return new ModelAndView(PagesPathEnum.PRODUCT_PAGE.getPath(), model);
    }
}

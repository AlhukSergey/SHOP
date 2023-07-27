package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.domain.Product;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.enums.RequestParamsEnum;
import by.teachmeskills.shop.repositories.CategoryRepository;
import by.teachmeskills.shop.services.CategoryService;
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

import static by.teachmeskills.shop.enums.RequestParamsEnum.CATEGORIES;
import static by.teachmeskills.shop.enums.RequestParamsEnum.IMAGES;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final ImageService imageService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductService productService, ImageService imageService) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
        this.imageService = imageService;
    }

    @Override
    public Category create(Category entity) {
        return categoryRepository.create(entity);
    }

    @Override
    public List<Category> read() {
        return categoryRepository.read();
    }

    @Override
    public Category update(Category entity) {
        return categoryRepository.update(entity);
    }

    @Override
    public void delete(int id) {
        categoryRepository.delete(id);
    }

    @Override
    public ModelAndView getCategoryById(int id) {
        ModelMap model = new ModelMap();

        Category category = categoryRepository.findById(id);
        if (Optional.ofNullable(category).isPresent()) {

            List<Product> products = productService.getProductsByCategoryId(category.getId());
            List<List<Image>> images = new ArrayList<>();

            for (Product product : products) {
                images.add(imageService.getImagesByProductId(product.getId()));
            }

            model.addAttribute(RequestParamsEnum.PRODUCTS.getValue(), products);
            model.addAttribute(IMAGES.getValue(), images.stream().flatMap(Collection::stream).collect(Collectors.toList()));
        }

        return new ModelAndView(PagesPathEnum.CATEGORY_PAGE.getPath(), model);
    }

    @Override
    public ModelAndView getCategories() {
        ModelMap model = new ModelMap();
        List<Category> categories = categoryRepository.read();
        List<Image> images = new ArrayList<>();

        for (Category category : categories) {
            images.add(imageService.getImageByCategoryId(category.getId()));
        }

        model.addAttribute(CATEGORIES.getValue(), categories);
        model.addAttribute(IMAGES.getValue(), images);

        model.addAttribute(RequestParamsEnum.CATEGORIES.getValue(), categories);
        return new ModelAndView(PagesPathEnum.HOME_PAGE.getPath(), model);
    }
}

package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.csv.CategoryCsv;
import by.teachmeskills.shop.csv.converters.CategoryCsvConverter;
import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.domain.Product;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.enums.RequestParamsEnum;
import by.teachmeskills.shop.enums.ShopConstants;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import by.teachmeskills.shop.exceptions.ParsingException;
import by.teachmeskills.shop.repositories.CategoryRepository;
import by.teachmeskills.shop.services.CategoryService;
import by.teachmeskills.shop.services.ProductService;
import by.teachmeskills.shop.utils.FileService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static by.teachmeskills.shop.enums.RequestParamsEnum.CATEGORIES;
import static by.teachmeskills.shop.enums.RequestParamsEnum.IMAGES;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final FileService<Category> fileService;

    private final CategoryCsvConverter categoryConverter;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductService productService, FileService<Category> fileService, CategoryCsvConverter categoryCsvConverter) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
        this.fileService = fileService;
        this.categoryConverter = categoryCsvConverter;
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
    public void delete(int id) throws EntityNotFoundException {
        categoryRepository.delete(id);
    }

    @Override
    public ModelAndView getCategoryById(int id) throws EntityNotFoundException {
        ModelMap model = new ModelMap();

        Category category = categoryRepository.findById(id);
        if (category != null) {

            List<Product> products = productService.getProductsByCategoryId(category.getId());

            model.addAttribute(RequestParamsEnum.PRODUCTS.getValue(), products);
        } else {
            throw new EntityNotFoundException("Категории не найдены. Попробуйте позже.");
        }

        return new ModelAndView(PagesPathEnum.CATEGORY_PAGE.getPath(), model);
    }

    @Override
    public ModelAndView getCategories() throws EntityNotFoundException {
        ModelMap model = new ModelMap();
        List<Category> categories = categoryRepository.read();
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Категории не найдены. Попробуйте позже.");
        }

        List<Image> images = new ArrayList<>();

        for (Category category : categories) {
            images.add(category.getImage());
        }

        model.addAttribute(CATEGORIES.getValue(), categories);
        model.addAttribute(IMAGES.getValue(), images);

        model.addAttribute(RequestParamsEnum.CATEGORIES.getValue(), categories);
        return new ModelAndView(PagesPathEnum.HOME_PAGE.getPath(), model);
    }

    @Override
    public ModelAndView getPaginatedCategories(int currentPage) throws EntityNotFoundException {
        ModelMap model = new ModelMap();
        List<Category> categories = categoryRepository.findPaginatedCategories(currentPage - 1, ShopConstants.PAGE_SIZE);
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Категории не найдены. Попробуйте позже.");
        }

        List<Image> images = new ArrayList<>();

        for (Category category : categories) {
            images.add(category.getImage());
        }

        Long totalItems = categoryRepository.getTotalItems();
        int totalPages = (int) (Math.ceil(totalItems / ShopConstants.PAGE_SIZE));

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);

        model.addAttribute(CATEGORIES.getValue(), categories);
        model.addAttribute(IMAGES.getValue(), images);

        model.addAttribute(RequestParamsEnum.CATEGORIES.getValue(), categories);
        return new ModelAndView(PagesPathEnum.HOME_PAGE.getPath(), model);
    }

    @Override
    public List<CategoryCsv> saveCategoriesFromFile(MultipartFile file) {
        List<CategoryCsv> csvCategories = parseCsv(file);
        List<Category> categories = Optional.ofNullable(csvCategories)
                .map(list -> list.stream()
                        .map(categoryConverter::fromCsv)
                        .toList())
                .orElse(null);
        if (Optional.ofNullable(categories).isPresent()) {
            categories.forEach(categoryRepository::create);
            return categories.stream().map(categoryConverter::toCsv).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public String saveCategoriesFromBD(String fileName) throws ExportToFIleException {
        return fileService.writeToFile(fileName, categoryRepository.read());
    }

    private List<CategoryCsv> parseCsv(MultipartFile file) {
        if (Optional.ofNullable(file).isPresent()) {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<CategoryCsv> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(CategoryCsv.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withSeparator(',')
                        .build();

                return csvToBean.parse();
            } catch (Exception ex) {
                throw new ParsingException(String.format("Ошибка во время преобразования данных данных: %s", ex.getMessage()));
            }
        }
        return Collections.emptyList();
    }
}

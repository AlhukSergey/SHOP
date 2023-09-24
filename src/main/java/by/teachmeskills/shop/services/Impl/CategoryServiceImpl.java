package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.csv.converters.CategoryCsvConverter;
import by.teachmeskills.shop.csv.dto.CategoryCsv;
import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.domain.Product;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.enums.RequestParamsEnum;
import by.teachmeskills.shop.enums.ShopConstants;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import by.teachmeskills.shop.exceptions.ParsingException;
import by.teachmeskills.shop.repositories.CategoryRepository;
import by.teachmeskills.shop.repositories.ProductRepository;
import by.teachmeskills.shop.services.CategoryService;
import by.teachmeskills.shop.services.ProductService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final CategoryCsvConverter categoryConverter;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductService productService, ProductRepository productRepository, CategoryCsvConverter categoryCsvConverter) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.categoryConverter = categoryCsvConverter;
    }

    @Override
    public Category create(Category entity) {
        return categoryRepository.save(entity);
    }

    @Override
    public ModelAndView getCategoryById(int id, int pageNumber, int pageSize) throws EntityNotFoundException {
        ModelMap model = new ModelMap();

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Категории с id %d не найдено. Попробуйте позже.", id)));

        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
        List<Product> products = productService.getProductsByCategoryId(category.getId(), paging);
        if (products.isEmpty()) {
            throw new EntityNotFoundException("Продукты не найдены. Попробуйте позже.");
        }
        int totalItems = productRepository.findAllByCategoryId(id).size();
        int totalPages = (int) (Math.ceil((double) totalItems / pageSize));

        model.addAttribute(RequestParamsEnum.PRODUCTS.getValue(), products);
        model.addAttribute(RequestParamsEnum.CATEGORY_ID.getValue(), id);
        model.addAttribute(RequestParamsEnum.PAGE_NUMBER.getValue(), pageNumber + 1);
        model.addAttribute(RequestParamsEnum.PAGE_SIZE.getValue(), ShopConstants.PAGE_SIZE);
        model.addAttribute(RequestParamsEnum.SELECTED_PAGE_SIZE.getValue(), pageSize);
        model.addAttribute(RequestParamsEnum.TOTAL_PAGES.getValue(), totalPages);
        return new ModelAndView(PagesPathEnum.CATEGORY_PAGE.getPath(), model);
    }

    @Override
    public ModelAndView getAllCategories(int pageNumber, int pageSize) throws EntityNotFoundException {
        ModelMap model = new ModelMap();

        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
        List<Category> categories = categoryRepository.findAll(paging).getContent();
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Категории не найдены. Попробуйте позже.");
        }
        long totalItems = categoryRepository.count();
        int totalPages = (int) (Math.ceil((double) totalItems / pageSize));

        model.addAttribute(RequestParamsEnum.PAGE_NUMBER.getValue(), pageNumber + 1);
        model.addAttribute(RequestParamsEnum.PAGE_SIZE.getValue(), ShopConstants.PAGE_SIZE);
        model.addAttribute(RequestParamsEnum.SELECTED_PAGE_SIZE.getValue(), pageSize);
        model.addAttribute(RequestParamsEnum.TOTAL_PAGES.getValue(), totalPages);
        model.addAttribute(RequestParamsEnum.CATEGORIES.getValue(), categories);
        return new ModelAndView(PagesPathEnum.HOME_PAGE.getPath(), model);
    }

    @Override
    public ModelAndView importCategoriesFromCsv(int pageNumber, int pageSize, MultipartFile file) throws EntityNotFoundException {
        List<CategoryCsv> csvCategories = parseCsv(file);
        List<Category> newCategories = Optional.ofNullable(csvCategories)
                .map(list -> list.stream()
                        .map(categoryConverter::fromCsv)
                        .toList())
                .orElse(null);

        if (Optional.ofNullable(newCategories).isPresent()) {
            newCategories.forEach(categoryRepository::save);
        }

        return getAllCategories(pageNumber, pageSize);
    }

    @Override
    public void exportCategoriesToCsv(HttpServletResponse response) throws ExportToFIleException {
        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=categories.csv";
        response.setHeader(headerKey, headerValue);
        response.setCharacterEncoding("UTF-8");

        List<CategoryCsv> csvCategories = categoryRepository.findAll().stream().map(categoryConverter::toCsv).toList();

        try (ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE)) {
            String[] csvHeader = {"Category ID", "Name", "Image path"};
            String[] nameMapping = {"id", "name", "imagePath"};

            csvWriter.writeHeader(csvHeader);

            for (CategoryCsv categoryCsv : csvCategories) {
                csvWriter.write(categoryCsv, nameMapping);
            }
        } catch (IOException e) {
            throw new ExportToFIleException("Во время записи в файл произошла непредвиденная ошибка. Попробуйте позже.");
        }
    }

    private List<CategoryCsv> parseCsv(MultipartFile file) {
        if (Optional.ofNullable(file).isPresent()) {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<CategoryCsv> csvToBean = new CsvToBeanBuilder<CategoryCsv>(reader)
                        .withType(CategoryCsv.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withSeparator(',')
                        .build();

                return csvToBean.parse();
            } catch (Exception ex) {
                throw new ParsingException(String.format("Ошибка во время преобразования данных: %s", ex.getMessage()));
            }
        }
        return Collections.emptyList();
    }
}

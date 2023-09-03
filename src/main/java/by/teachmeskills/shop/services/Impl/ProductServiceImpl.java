package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.csv.ProductCsv;
import by.teachmeskills.shop.csv.converters.ProductCsvConverter;
import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.domain.Product;
import by.teachmeskills.shop.enums.InfoEnum;
import by.teachmeskills.shop.enums.PagesPathEnum;
import by.teachmeskills.shop.enums.RequestParamsEnum;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import by.teachmeskills.shop.exceptions.ParsingException;
import by.teachmeskills.shop.repositories.ProductRepository;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static by.teachmeskills.shop.enums.RequestParamsEnum.IMAGES;
import static by.teachmeskills.shop.enums.RequestParamsEnum.PRODUCTS;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductCsvConverter productConverter;
    private final FileService<Product> fileService;

    public ProductServiceImpl(ProductRepository productRepository, ProductCsvConverter productConverter, FileService<Product> fileService) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
        this.fileService = fileService;
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
    public void delete(int id) throws EntityNotFoundException {
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
                images.add(product.getImages());
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
        if (product != null) {
            model.addAttribute(RequestParamsEnum.PRODUCT.getValue(), product);
            model.addAttribute(IMAGES.getValue(), product.getImages());
        }
        return new ModelAndView(PagesPathEnum.PRODUCT_PAGE.getPath(), model);
    }

    @Override
    public List<ProductCsv> saveProductsFromFile(MultipartFile file) {
        List<ProductCsv> csvProducts = parseCsv(file);
        List<Product> orders = Optional.ofNullable(csvProducts)
                .map(list -> list.stream()
                        .map(productConverter::fromCsv)
                        .toList())
                .orElse(null);
        if (Optional.ofNullable(orders).isPresent()) {
            orders.forEach(productRepository::create);
            return orders.stream().map(productConverter::toCsv).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public String saveProductsFromBD(String fileName) throws ExportToFIleException {
        return fileService.writeToFile(fileName, productRepository.read());
    }

    private List<ProductCsv> parseCsv(MultipartFile file) {
        if (Optional.ofNullable(file).isPresent()) {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<ProductCsv> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(ProductCsv.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withSeparator(',')
                        .build();

                return csvToBean.parse();
            } catch (Exception ex) {
                throw new ParsingException(String.format("Ошибка во время парсинга данных: %s", ex.getMessage()));
            }
        }
        return Collections.emptyList();
    }
}

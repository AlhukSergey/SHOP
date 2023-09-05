package by.teachmeskills.shop.csv.converters;

import by.teachmeskills.shop.csv.dto.ProductCsv;
import by.teachmeskills.shop.domain.Product;
import by.teachmeskills.shop.repositories.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductCsvConverter {
    private final CategoryRepository categoryRepository;
    private final ImageCsvConverter imageCsvConverter;

    public ProductCsvConverter(CategoryRepository categoryRepository, ImageCsvConverter imageCsvConverter) {
        this.categoryRepository = categoryRepository;
        this.imageCsvConverter = imageCsvConverter;
    }

    public ProductCsv toCsv(Product product) {
        return Optional.ofNullable(product).map(p -> ProductCsv.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .price(p.getPrice())
                        .categoryId(p.getCategory().getId())
                        .images(Optional.ofNullable(p.getImages())
                                .map(images -> images.stream()
                                        .map(imageCsvConverter::toCsv)
                                        .toList())
                                .orElse(List.of()))
                        .build())
                .orElse(null);
    }

    public Product fromCsv(ProductCsv productCsv) {
        return Optional.ofNullable(productCsv).map(pd -> Product.builder()
                        .name(pd.getName())
                        .description(pd.getDescription())
                        .price(pd.getPrice())
                        .category(categoryRepository.findById(pd.getCategoryId()))
                        .images(Optional.ofNullable(pd.getImages())
                                .map(images -> images
                                        .stream()
                                        .map(imageCsvConverter::fromCsv).toList())
                                .orElse(List.of()))
                        .build())
                .orElse(null);
    }
}

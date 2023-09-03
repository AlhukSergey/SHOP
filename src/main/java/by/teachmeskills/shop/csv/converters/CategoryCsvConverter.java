package by.teachmeskills.shop.csv.converters;

import by.teachmeskills.shop.csv.CategoryCsv;
import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.domain.Image;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryCsvConverter {
    private final ProductCsvConverter productConverter;

    public CategoryCsvConverter(ProductCsvConverter productConverter) {
        this.productConverter = productConverter;
    }

    public CategoryCsv toCsv(Category category) {
        return Optional.ofNullable(category).map(c -> CategoryCsv.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .imagePath(c.getImage().getImagePath())
                        .products(Optional.ofNullable(c.getProducts()).map(products -> products.stream().map(productConverter::toCsv).toList()).orElse(List.of()))
                        .build())
                .orElse(null);
    }

    public Category fromCsv(CategoryCsv categoryCsv) {
        return Optional.ofNullable(categoryCsv).map(cc -> Category.builder()
                        .name(cc.getName())
                        .image(Image.builder()
                                .imagePath(cc.getImagePath())
                                .primary(1)
                                .build())
                        .build())
                .orElse(null);
    }
}

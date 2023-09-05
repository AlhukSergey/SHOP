package by.teachmeskills.shop.csv.converters;

import by.teachmeskills.shop.csv.dto.CategoryCsv;
import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.domain.Image;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryCsvConverter {
    public CategoryCsv toCsv(Category category) {
        return Optional.ofNullable(category).map(c -> CategoryCsv.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .imagePath(c.getImage().getImagePath())
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

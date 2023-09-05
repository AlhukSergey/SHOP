package by.teachmeskills.shop.csv.converters;

import by.teachmeskills.shop.csv.dto.ImageCsv;
import by.teachmeskills.shop.domain.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageCsvConverter {
    public ImageCsv toCsv(Image image) {
        return ImageCsv.builder()
                .id(image.getId())
                .imagePath(image.getImagePath())
                .primary(image.getPrimary())
                .build();
    }

    public Image fromCsv(ImageCsv imageCsv) {
        return Image.builder()
                .imagePath(imageCsv.getImagePath())
                .primary(imageCsv.getPrimary())
                .build();
    }
}

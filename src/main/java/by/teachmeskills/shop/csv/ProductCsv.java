package by.teachmeskills.shop.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductCsv extends BaseCsv {
    @CsvBindByName
    private String name;

    @CsvBindByName
    private String description;

    @CsvBindByName
    private double price;

    @CsvBindByName
    private List<ImageCsv> images;

    @CsvBindByName
    private int categoryId;
}

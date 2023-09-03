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
public class CategoryCsv extends BaseCsv {
    @CsvBindByName
    private String name;

    @CsvBindByName
    private String imagePath;

    private List<ProductCsv> products;
}

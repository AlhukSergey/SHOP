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
public class OrderCsv extends BaseCsv {
    @CsvBindByName
    private String orderStatus;

    @CsvBindByName
    private String createdAt;

    @CsvBindByName
    private List<ProductCsv> products;

    @CsvBindByName
    private double price;

    @CsvBindByName
    private int userId;
}

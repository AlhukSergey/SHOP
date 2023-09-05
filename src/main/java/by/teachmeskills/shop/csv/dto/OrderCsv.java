package by.teachmeskills.shop.csv.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCsv {
    private int id;

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

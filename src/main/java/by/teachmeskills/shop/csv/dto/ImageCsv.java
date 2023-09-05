package by.teachmeskills.shop.csv.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageCsv {
    private int id;

    @CsvBindByName
    private String imagePath;

    @CsvBindByName
    private int primary;
}

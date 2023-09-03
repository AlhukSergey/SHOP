package by.teachmeskills.shop.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserCsv extends BaseCsv {
    @CsvBindByName
    private String name;

    @CsvBindByName
    private String surname;

    @CsvBindByName
    private LocalDate birthday;

    @CsvBindByName
    private double balance;

    @CsvBindByName
    private String email;

    @CsvBindByName
    private String password;

    private List<OrderCsv> orders;
}

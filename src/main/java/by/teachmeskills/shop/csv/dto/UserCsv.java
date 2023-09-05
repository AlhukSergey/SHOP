package by.teachmeskills.shop.csv.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCsv {
    private int id;

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

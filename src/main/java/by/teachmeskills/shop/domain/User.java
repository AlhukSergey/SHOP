package by.teachmeskills.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    private String name;
    private String surname;
    private LocalDate birthday;
    private double balance;
    private String email;
    private String password;
}

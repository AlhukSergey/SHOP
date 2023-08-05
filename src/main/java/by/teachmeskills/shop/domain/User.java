package by.teachmeskills.shop.domain;

import by.teachmeskills.shop.utils.beanvalidationgroup.Login;
import by.teachmeskills.shop.utils.beanvalidationgroup.Registration;
import by.teachmeskills.shop.utils.beanvalidationgroup.UpdateDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    @NotBlank(message = "Поле должно быть заполнено!", groups = {Registration.class, UpdateDate.class})
    @Size(min = 3, max = 100, message = "Имя не может содержать меньше 3 и больше 100 символов.", groups = {Registration.class, UpdateDate.class})
    private String name;

    @NotBlank(message = "Поле должно быть заполнено!", groups = {Registration.class, UpdateDate.class})
    @Size(min = 3, max = 100, message = "Фамилия не может содержать меньше 3 и больше 100 символов.", groups = {Registration.class, UpdateDate.class})
    private String surname;

    @Past(groups = {Registration.class, UpdateDate.class})
    private LocalDate birthday;

    private double balance;

    //checkEmailFormat Email example (used for login) : anna18@gmail.com
    @Email(regexp = "^(.+)@(\\S+)$", message = "Неверный формат email.", groups = {Registration.class, UpdateDate.class})
    @NotBlank(message = "Поле должно быть заполнено!", groups = {Login.class, Registration.class, UpdateDate.class})
    private String email;
    /*
       (?=.*[0-9]) a digit must occur at least once
       (?=.*[a-z]) a lower case letter must occur at least once
       (?=.*[A-Z]) an upper case letter must occur at least once
       (?=.*[@#$%^&+=]) a special character must occur at least once
       (?=\\S+$) no whitespace allowed in the entire string
       .{8,} at least 8 characters
       Password example (used for login) : A!1+=asasasaas
       */
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Неверный формат пароля! " +
            "Длина пароля должна быть не короче 8 символов. Пароль должен содержать как минимум одну цифру," +
            "одну заглавную букву, одну букву нижнего регистра, один специальный символ.", groups = Registration.class)
    @NotBlank(message = "Поле должно быть заполнено!", groups = {Login.class, Registration.class})
    private String password;
}

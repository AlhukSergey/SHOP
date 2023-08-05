package by.teachmeskills.shop.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity{
    @NotBlank(message = "Поле должно быть заполнено!")
    @Size(min = 3, max = 100, message = "Имя продукта не может содержать меньше 3 и больше 100 символов.")
    private String name;
    @NotBlank(message = "Поле должно быть заполнено!")
    @Size(min = 5, message = "Описание продукта не может содержать меньше 5 символов.")
    private String description;
    @NotBlank(message = "Поле должно быть заполнено!")
    @Min(value = 0)
    private double price;
    private int categoryId;
}
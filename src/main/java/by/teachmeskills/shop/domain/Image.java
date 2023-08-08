package by.teachmeskills.shop.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class Image extends BaseEntity {
    @NotBlank(message = "Поле должно быть заполнено!")
    @Size(min = 10, message = "Путь к картинке не может содержать меньше 3 и больше 100 символов.")
    private String imagePath;
    private int categoryId;
    private int productId;
    @NotBlank(message = "Поле должно быть заполнено!")
    @Min(value = 0, message = "Поле может принимать значение 0 (второстепенное изображение) либо 1 (главное изображение).")
    @Max(value = 1, message = "Поле может принимать значение 0 (второстепенное изображение) либо 1 (главное изображение).")
    private int primary;
}

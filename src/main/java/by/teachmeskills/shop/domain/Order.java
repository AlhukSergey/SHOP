package by.teachmeskills.shop.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity{
    private int id;
    private int userId;
    private OrderStatus orderStatus;
    @PastOrPresent
    private LocalDateTime createdAt;
    private List<Product> productList;
    @NotBlank(message = "Поле должно быть заполнено!")
    @Min(value = 0)
    private double price;
}

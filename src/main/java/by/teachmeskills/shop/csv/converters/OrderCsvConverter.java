package by.teachmeskills.shop.csv.converters;

import by.teachmeskills.shop.csv.OrderCsv;
import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.domain.OrderStatus;
import by.teachmeskills.shop.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class OrderCsvConverter {
    private final ProductCsvConverter productConverter;
    private final UserRepository userRepository;

    public OrderCsvConverter(ProductCsvConverter productCsvConverter, UserRepository userRepository) {
        this.productConverter = productCsvConverter;
        this.userRepository = userRepository;
    }

    public OrderCsv toCsv(Order order) {
        return Optional.ofNullable(order).map(o -> OrderCsv.builder()
                        .id(o.getId())
                        .orderStatus(o.getOrderStatus().toString())
                        .createdAt(o.getCreatedAt().toString())
                        .products(Optional.ofNullable(o.getProducts()).map(products -> products.stream()
                                .map(productConverter::toCsv).toList()).orElse(List.of()))
                        .price(o.getPrice())
                        .userId(o.getUser().getId())
                        .build())
                .orElse(null);
    }

    public Order fromCsv(OrderCsv orderCsv) {
        return Order.builder()
                .user(Optional.ofNullable(userRepository.findById(orderCsv.getUserId()))
                        .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено.", orderCsv.getUserId()))))
                .orderStatus(orderStatusConverter(orderCsv.getOrderStatus()))
                .price(orderCsv.getPrice())
                .createdAt(Timestamp.valueOf(LocalDateTime.parse(orderCsv.getCreatedAt())))
                .build();
    }

    private OrderStatus orderStatusConverter(String orderStatus) {
        return switch (orderStatus) {
            case "ACTIVE" -> OrderStatus.ACTIVE;
            case "FINISHED" -> OrderStatus.FINISHED;
            default -> null;
        };
    }
}

package by.teachmeskills.shop.csv.converters;

import by.teachmeskills.shop.csv.dto.UserCsv;
import by.teachmeskills.shop.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserCsvConverter {
    private final OrderCsvConverter orderConverter;

    public UserCsvConverter(OrderCsvConverter orderCsvConverter) {
        this.orderConverter = orderCsvConverter;
    }

    public UserCsv toCsv(User user) {
        return Optional.ofNullable(user).map(u -> UserCsv.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .surname(u.getSurname())
                        .birthday(u.getBirthday())
                        .email(u.getEmail())
                        .balance(u.getBalance())
                        .password(u.getPassword())
                        .orders(Optional.ofNullable(u.getOrders()).map(orders -> orders.stream()
                                .map(orderConverter::toCsv).toList()).orElse(List.of()))
                        .build())
                .orElse(null);
    }

    public User fromCsv(UserCsv userCsv) {
        return Optional.ofNullable(userCsv).map(uc -> User.builder()
                        .name(uc.getName())
                        .surname(uc.getSurname())
                        .birthday(uc.getBirthday())
                        .email(uc.getEmail())
                        .password(uc.getPassword())
                        .balance(uc.getBalance())
                        .build())
                .orElse(null);
    }
}

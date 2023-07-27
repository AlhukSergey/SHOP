package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService extends BaseService<Order> {
    Order getOrderById(int id);

    List<Order> getOrderByDate(LocalDateTime date);
    List<Order> getOrdersByUserId(int id);
}

package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.repositories.OrderRepository;
import by.teachmeskills.shop.services.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order create(Order entity) {
        return orderRepository.create(entity);
    }

    @Override
    public List<Order> read() {
        return orderRepository.read();
    }

    @Override
    public Order update(Order entity) {
        return orderRepository.update(entity);
    }

    @Override
    public void delete(int id) {
        orderRepository.delete(id);
    }

    @Override
    public Order getOrderById(int id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getOrderByDate(LocalDateTime date) {
        return orderRepository.findByDate(date);
    }

    @Override
    public List<Order> getOrdersByUserId(int id) {
        return orderRepository.findByUserId(id);
    }
}

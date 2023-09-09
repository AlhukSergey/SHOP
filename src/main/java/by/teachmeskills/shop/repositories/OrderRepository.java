package by.teachmeskills.shop.repositories;

import by.teachmeskills.shop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findById(int id);
    List<Order> findByCreatedAt(LocalDateTime date);
    List<Order> findByUserId(int id);
}

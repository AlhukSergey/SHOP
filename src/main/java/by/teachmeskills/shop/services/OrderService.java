package by.teachmeskills.shop.services;

import by.teachmeskills.shop.csv.OrderCsv;
import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService extends BaseService<Order> {
    Order getOrderById(int id);

    List<Order> getOrderByDate(LocalDateTime date);

    List<Order> getOrdersByUserId(int id);

    List<OrderCsv> saveOrdersFromFile(MultipartFile file) throws Exception;

    String saveUserOrdersFromBD(int userId, String fileName) throws ExportToFIleException;
}

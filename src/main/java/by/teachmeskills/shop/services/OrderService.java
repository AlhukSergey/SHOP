package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService extends BaseService<Order> {
    Order getOrderById(int id);

    List<Order> getOrderByDate(LocalDateTime date);

    List<Order> getOrdersByUserId(int id);

    ModelAndView saveOrdersFromFile(MultipartFile file, User user);

    void saveUserOrdersFromBD(HttpServletResponse response, int userId) throws ExportToFIleException;
}

package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.csv.OrderCsv;
import by.teachmeskills.shop.csv.converters.OrderCsvConverter;
import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.exceptions.ExportToFIleException;
import by.teachmeskills.shop.exceptions.ParsingException;
import by.teachmeskills.shop.repositories.OrderRepository;
import by.teachmeskills.shop.services.OrderService;
import by.teachmeskills.shop.utils.FileService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderCsvConverter orderConverter;
    private final FileService<Order> fileService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderCsvConverter orderConverter, FileService<Order> fileService) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.fileService = fileService;
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
    public void delete(int id) throws EntityNotFoundException {
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


    @Override
    public List<OrderCsv> saveOrdersFromFile(MultipartFile file) {
        List<OrderCsv> csvOrders = parseCsv(file);
        List<Order> orders = Optional.ofNullable(csvOrders)
                .map(list -> list.stream()
                        .map(orderConverter::fromCsv)
                        .toList())
                .orElse(null);
        if (Optional.ofNullable(orders).isPresent()) {
            orders.forEach(orderRepository::create);
            return orders.stream().map(orderConverter::toCsv).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public String saveUserOrdersFromBD(int userId, String fileName) throws ExportToFIleException {
        return fileService.writeToFile(fileName, orderRepository.findByUserId(userId));
    }

    private List<OrderCsv> parseCsv(MultipartFile file) {
        if (Optional.ofNullable(file).isPresent()) {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<OrderCsv> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(OrderCsv.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withSeparator(',')
                        .build();

                return csvToBean.parse();
            } catch (Exception ex) {
                throw new ParsingException(String.format("Ошибка во время парсинга данных: %s", ex.getMessage()));
            }
        }
        return Collections.emptyList();
    }
}

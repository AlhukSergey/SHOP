package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.domain.OrderStatus;
import by.teachmeskills.shop.repositories.OrderRepository;
import by.teachmeskills.shop.utils.ConverterUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private static final String ADD_ORDER_QUERY = "INSERT INTO orders (userId, createdAt, status, price) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_ORDER_QUERY = "UPDATE orders SET status = ? WHERE id = ?";
    private static final String GET_ALL_ORDERS_QUERY = "SELECT * FROM orders";
    private static final String GET_ALL_ORDERS_BY_USER_ID_QUERY = "SELECT * FROM orders WHERE userId = ?";
    private static final String GET_ORDER_BY_ID_QUERY = "SELECT * FROM orders WHERE id = ?";
    private static final String GET_ORDERS_BY_DATE_QUERY = "SELECT * FROM orders WHERE createAt = ?";
    private static final String DELETE_ORDER_QUERY = "UPDATE orders SET status = ? WHERE id = ?";
    @SneakyThrows(Exception.class)
    @Override
    public Order create(Order entity) {
        Connection connection = pool.getConnection();
        PreparedStatement psInsert = connection.prepareStatement(ADD_ORDER_QUERY);

        psInsert.setInt(1, entity.getUserId());
        psInsert.setTimestamp(2, Timestamp.valueOf(entity.getCreatedAt()));
        psInsert.setString(3, entity.getOrderStatus().toString());
        psInsert.setDouble(4, entity.getPrice());
        psInsert.execute();

        pool.closeConnection(connection);
        psInsert.close();
        return entity;
    }
    @SneakyThrows(Exception.class)
    @Override
    public List<Order> read() {
        List<Order> orders = new ArrayList<>();
        Connection connection = pool.getConnection();
        PreparedStatement psGet = connection.prepareStatement(GET_ALL_ORDERS_QUERY);

        ResultSet resultSet = psGet.executeQuery();
        while (resultSet.next()) {
            orders.add(Order.builder()
                    .id(resultSet.getInt(1))
                    .userId(resultSet.getInt(2))
                    .createdAt(resultSet.getTimestamp(3).toLocalDateTime())
                    .orderStatus(ConverterUtils.toOrderStatus(resultSet.getString(4)))
                    .price(resultSet.getDouble(5))
                    .build()
            );
        }
        resultSet.close();

        pool.closeConnection(connection);
        psGet.close();
        return orders;
    }
    @SneakyThrows(Exception.class)
    @Override
    public Order update(Order entity) {
        Connection connection = pool.getConnection();
        PreparedStatement psUpdate = connection.prepareStatement(UPDATE_ORDER_QUERY);

        psUpdate.setString(1, entity.getOrderStatus().toString());
        psUpdate.setInt(2, entity.getId());
        psUpdate.execute();

        pool.closeConnection(connection);
        psUpdate.close();
        return entity;
    }
    @SneakyThrows(Exception.class)
    @Override
    public void delete(int id) {
        Connection connection = pool.getConnection();
        PreparedStatement psDelete = connection.prepareStatement(DELETE_ORDER_QUERY);

        psDelete.setInt(1, id);
        psDelete.setString(2, String.valueOf(OrderStatus.FINISHED));
        psDelete.execute();

        pool.closeConnection(connection);
        psDelete.close();
    }
    @SneakyThrows(Exception.class)
    @Override
    public Order findById(int id) {
        Order order = null;
        Connection connection = pool.getConnection();
        PreparedStatement psGet = connection.prepareStatement(GET_ORDER_BY_ID_QUERY);

        psGet.setInt(1, id);
        ResultSet resultSet = psGet.executeQuery();
        while (resultSet.next()) {
            order = Order.builder()
                    .id(resultSet.getInt(1))
                    .userId(resultSet.getInt(2))
                    .createdAt(resultSet.getTimestamp(3).toLocalDateTime())
                    .orderStatus(ConverterUtils.toOrderStatus(resultSet.getString(4)))
                    .price(resultSet.getDouble(5))
                    .build();
        }
        resultSet.close();

        pool.closeConnection(connection);
        psGet.close();
        return order;
    }
    @SneakyThrows(Exception.class)
    @Override
    public List<Order> findByDate(LocalDateTime date) {
        List<Order> orders = new ArrayList<>();
        Connection connection = pool.getConnection();
        PreparedStatement psGet = connection.prepareStatement(GET_ORDERS_BY_DATE_QUERY);
        psGet.setTimestamp(1, Timestamp.valueOf(date));

        ResultSet resultSet = psGet.executeQuery();
        while (resultSet.next()) {
            orders.add(Order.builder()
                    .id(resultSet.getInt(1))
                    .userId(resultSet.getInt(2))
                    .createdAt(resultSet.getTimestamp(3).toLocalDateTime())
                    .orderStatus(ConverterUtils.toOrderStatus(resultSet.getString(4)))
                    .price(resultSet.getDouble(5))
                    .build()
            );
        }
        resultSet.close();

        pool.closeConnection(connection);
        psGet.close();
        return orders;
    }
    @SneakyThrows(Exception.class)
    @Override
    public List<Order> findByUserId(int id) {
        List<Order> orders = new ArrayList<>();
        Connection connection = pool.getConnection();
        PreparedStatement psGet = connection.prepareStatement(GET_ALL_ORDERS_BY_USER_ID_QUERY);
        psGet.setInt(1, id);

        ResultSet resultSet = psGet.executeQuery();
        while (resultSet.next()) {
            orders.add(Order.builder()
                    .id(resultSet.getInt(1))
                    .userId(resultSet.getInt(2))
                    .createdAt(resultSet.getTimestamp(3).toLocalDateTime())
                    .orderStatus(ConverterUtils.toOrderStatus(resultSet.getString(4)))
                    .price(resultSet.getDouble(5))
                    .build()
            );
        }
        resultSet.close();

        pool.closeConnection(connection);
        psGet.close();
        return orders;
    }
}

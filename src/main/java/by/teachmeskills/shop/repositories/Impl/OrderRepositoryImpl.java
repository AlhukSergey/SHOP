package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.domain.OrderStatus;
import by.teachmeskills.shop.repositories.OrderRepository;
import by.teachmeskills.shop.utils.ConverterUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final String ADD_ORDER_QUERY = "INSERT INTO orders (userId, createdAt, status, price) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_ORDER_QUERY = "UPDATE orders SET status = ? WHERE id = ?";
    private static final String GET_ALL_ORDERS_QUERY = "SELECT * FROM orders";
    private static final String GET_ALL_ORDERS_BY_USER_ID_QUERY = "SELECT * FROM orders WHERE userId = ?";
    private static final String GET_ORDER_BY_ID_QUERY = "SELECT * FROM orders WHERE id = ?";
    private static final String GET_ORDERS_BY_DATE_QUERY = "SELECT * FROM orders WHERE createAt = ?";
    private static final String DELETE_ORDER_QUERY = "UPDATE orders SET status = ? WHERE id = ?";

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order create(Order entity) {
        return jdbcTemplate.execute(ADD_ORDER_QUERY, (PreparedStatementCallback<Order>) ps -> {
            ps.setInt(1, entity.getUserId());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getCreatedAt()));
            ps.setString(3, entity.getOrderStatus().toString());
            ps.setDouble(4, entity.getPrice());
            ps.execute();
            return entity;
        });
    }

    @Override
    public List<Order> read() {
        return jdbcTemplate.query(GET_ALL_ORDERS_QUERY, (rs, rowNum) -> Order.builder()
                .id(rs.getInt("id"))
                .userId(rs.getInt("userId"))
                .createdAt(rs.getTimestamp("createdAt").toLocalDateTime())
                .orderStatus(ConverterUtils.toOrderStatus(rs.getString("status")))
                .price(rs.getDouble("price"))
                .build());
    }

    @Override
    public Order update(Order entity) {
        return jdbcTemplate.execute(UPDATE_ORDER_QUERY, (PreparedStatementCallback<Order>) ps -> {
            ps.setString(1, entity.getOrderStatus().toString());
            ps.setInt(2, entity.getId());
            ps.execute();
            return entity;
        });
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_ORDER_QUERY, String.valueOf(OrderStatus.FINISHED), id);
    }

    @Override
    public Order findById(int id) {
        return jdbcTemplate.queryForObject(GET_ORDER_BY_ID_QUERY, (RowMapper<Order>) (rs, rowNum) -> Order.builder()
                .id(rs.getInt("id"))
                .userId(rs.getInt("userId"))
                .createdAt(rs.getTimestamp("createdAt").toLocalDateTime())
                .orderStatus(ConverterUtils.toOrderStatus(rs.getString("status")))
                .price(rs.getDouble("price"))
                .build(), id);
    }

    @Override
    public List<Order> findByDate(LocalDateTime date) {
        return jdbcTemplate.query(GET_ORDERS_BY_DATE_QUERY, (rs, rowNum) -> Order.builder()
                .id(rs.getInt("id"))
                .userId(rs.getInt("userId"))
                .createdAt(rs.getTimestamp("createdAt").toLocalDateTime())
                .orderStatus(ConverterUtils.toOrderStatus(rs.getString("status")))
                .price(rs.getDouble("price"))
                .build(), Timestamp.valueOf(date));
    }

    @Override
    public List<Order> findByUserId(int id) {
        return jdbcTemplate.query(GET_ALL_ORDERS_BY_USER_ID_QUERY, (rs, rowNum) -> Order.builder()
                .id(rs.getInt("id"))
                .userId(rs.getInt("userId"))
                .createdAt(rs.getTimestamp("createdAt").toLocalDateTime())
                .orderStatus(ConverterUtils.toOrderStatus(rs.getString("status")))
                .price(rs.getDouble("price"))
                .build(), id);
    }
}

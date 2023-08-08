package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Product;
import by.teachmeskills.shop.repositories.ProductRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final String GET_ALL_PRODUCTS_FOR_ORDER = "SELECT * FROM product p " +
            "JOIN order_lists ol ON p.id = ol.productId " +
            "JOIN orders o ON o.id = ol.orderId WHERE o.id = ?";

    private static final String ADD_PRODUCT_QUERY = "INSERT INTO products (name, description, price, categoryId) VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_PRODUCTS_QUERY = "SELECT * FROM products";
    private static final String DELETE_PRODUCT_QUERY = "DELETE FROM products WHERE id = ?";
    private static final String GET_PRODUCT_BY_ID_QUERY = "SELECT * FROM products WHERE id = ?";
    private static final String GET_PRODUCT_BY_CATEGORY_ID_QUERY = "SELECT * FROM products WHERE categoryId = ?";
    private static final String GET_PRODUCT_QUANTITY = "SELECT quantity FROM order_lists ol JOIN orders o ON ol.orderId = o.id WHERE orderId = ?";
    private static final String UPDATE_PRODUCT_PRICE_QUERY = "UPDATE products SET price = ? WHERE id = ?";

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Product create(Product entity) {
        return jdbcTemplate.execute(ADD_PRODUCT_QUERY, (PreparedStatementCallback<Product>) ps -> {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setDouble(3, entity.getPrice());
            ps.setInt(4, entity.getCategoryId());
            ps.execute();

            return entity;
        });
    }

    @Override
    public List<Product> read() {
        return jdbcTemplate.query(GET_ALL_PRODUCTS_QUERY, (rs, rowNum) -> Product.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .price(rs.getDouble("price"))
                .categoryId(rs.getInt("categoryId"))
                .build());
    }

    @Override
    public Product update(Product entity) {
        return jdbcTemplate.execute(UPDATE_PRODUCT_PRICE_QUERY, (PreparedStatementCallback<Product>) ps -> {
            ps.setDouble(1, entity.getPrice());
            ps.setInt(2, entity.getId());
            ps.execute();
            return entity;
        });
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_PRODUCT_QUERY, id);
    }

    @Override
    public Product findById(int id) {
        return jdbcTemplate.queryForObject(GET_PRODUCT_BY_ID_QUERY, (RowMapper<Product>) (rs, rowNum) -> Product.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .price(rs.getDouble("price"))
                .categoryId(rs.getInt("categoryId"))
                .build(), id);
    }

    @Override
    public List<Product> findByCategoryId(int categoryId) {
        return jdbcTemplate.query(GET_PRODUCT_BY_CATEGORY_ID_QUERY, (rs, rowNum) -> Product.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .price(rs.getDouble("price"))
                .categoryId(rs.getInt("categoryId"))
                .build(), categoryId);
    }

    @Override
    public List<Product> findBySearchParameter(String parameter) {
        return jdbcTemplate.query(generateSearchQuery(parameter), (rs, rowNum) -> Product.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .price(rs.getDouble("price"))
                .categoryId(rs.getInt("categoryId"))
                .build());
    }

    private String generateSearchQuery(String searchParameter) {
        StringBuilder query = new StringBuilder("SELECT * FROM products WHERE (LOWER (name) LIKE '%");

        return query.append(searchParameter)
                .append("%' OR LOWER (description) LIKE '%")
                .append(searchParameter)
                .append("%') ORDER BY name")
                .toString();
    }
}

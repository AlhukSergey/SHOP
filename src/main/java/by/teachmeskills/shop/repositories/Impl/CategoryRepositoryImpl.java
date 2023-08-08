package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.repositories.CategoryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final String ADD_CATEGORY_QUERY = "INSERT INTO categories (name) VALUES (?)";
    private static final String GET_ALL_CATEGORIES_QUERY = "SELECT * FROM categories";
    private static final String UPDATE_CATEGORY_QUERY = "UPDATE categories SET name = ? WHERE id = ?";
    private static final String DELETE_CATEGORY_QUERY = "DELETE FROM categories WHERE id = ?";
    private static final String GET_CATEGORY_BY_ID_QUERY = "SELECT * FROM categories WHERE id = ?";

    public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Category create(Category entity) {
        return jdbcTemplate.execute(ADD_CATEGORY_QUERY, (PreparedStatementCallback<Category>) ps -> {
            ps.setString(1, entity.getName());
            ps.execute();
            return entity;
        });
    }

    @Override
    public List<Category> read() {
        return jdbcTemplate.query(GET_ALL_CATEGORIES_QUERY,  (rs, rowNum) -> Category.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build());
    }

    @Override
    public Category update(Category entity) {
        return jdbcTemplate.execute(UPDATE_CATEGORY_QUERY, (PreparedStatementCallback<Category>) ps ->{
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getName());
            ps.execute();
            return entity;
        });
    }

    @Override
    public void delete(int id) {
       jdbcTemplate.update(DELETE_CATEGORY_QUERY, id);
    }

    @Override
    public Category findById(int id) {
        return jdbcTemplate.queryForObject(GET_CATEGORY_BY_ID_QUERY, (RowMapper<Category>) (rs, rowNum) -> Category.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build(), id);
    }
}

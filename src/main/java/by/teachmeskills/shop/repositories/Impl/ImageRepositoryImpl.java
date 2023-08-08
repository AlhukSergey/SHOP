package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.repositories.ImageRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageRepositoryImpl implements ImageRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final String ADD_IMAGE_QUERY = "INSERT INTO images (imagePath, categoryId, productId, primaryImage) VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_IMAGES_QUERY = "SELECT * FROM images";
    private static final String UPDATE_IMAGE_QUERY = "UPDATE images SET imagePath = ? WHERE id = ?";
    private static final String DELETE_IMAGE_QUERY = "DELETE FROM images WHERE id = ?";
    private static final String GET_IMAGE_BY_ID_QUERY = "SELECT * FROM images WHERE id = ?";
    private static final String GET_IMAGE_BY_CATEGORY_ID_QUERY = "SELECT * FROM images WHERE categoryId = ?";
    private static final String GET_IMAGES_BY_PRODUCT_ID_QUERY = "SELECT * FROM images WHERE productId = ?";

    public ImageRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Image create(Image entity) {
        return jdbcTemplate.execute(ADD_IMAGE_QUERY, (PreparedStatementCallback<Image>) ps -> {
            ps.setString(1, entity.getImagePath());
            ps.setInt(2, entity.getCategoryId());
            ps.setInt(3, entity.getProductId());
            ps.setInt(4, entity.getPrimary());
            ps.execute();
            return entity;
        });
    }

    @Override
    public List<Image> read() {
        return jdbcTemplate.query(GET_ALL_IMAGES_QUERY, (rs, rowNum) -> Image.builder()
                .id(rs.getInt("id"))
                .imagePath(rs.getString("imagePath"))
                .categoryId(rs.getInt("categoryId "))
                .productId(rs.getInt("productId"))
                .primary(rs.getInt("primaryImage"))
                .build());
    }

    @Override
    public Image update(Image entity) {
        return jdbcTemplate.execute(UPDATE_IMAGE_QUERY, (PreparedStatementCallback<Image>) ps -> {
            ps.setString(1, entity.getImagePath());
            ps.setInt(2, entity.getId());
            ps.execute();
            return entity;
        });
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_IMAGE_QUERY, id);
    }

    @Override
    public Image findById(int id) {
        return jdbcTemplate.queryForObject(GET_IMAGE_BY_ID_QUERY, (RowMapper<Image>) (rs, rowNum) -> Image.builder()
                .id(rs.getInt("id"))
                .imagePath(rs.getString("imagePath"))
                .categoryId(rs.getInt("categoryId"))
                .productId(rs.getInt("productId"))
                .primary(rs.getInt("primaryImage"))
                .build(), id);
    }

    @Override
    public Image findByCategoryId(int categoryId) {
        return jdbcTemplate.queryForObject(GET_IMAGE_BY_CATEGORY_ID_QUERY, (RowMapper<Image>) (rs, rowNum) -> Image.builder()
                .id(rs.getInt("id"))
                .imagePath(rs.getString("imagePath"))
                .categoryId(rs.getInt("categoryId"))
                .build(), categoryId);
    }

    @Override
    public List<Image> findByProductId(int productId) {
        return jdbcTemplate.query(GET_IMAGES_BY_PRODUCT_ID_QUERY, (rs, rowNum) -> Image.builder()
                .id(rs.getInt("id"))
                .imagePath(rs.getString("imagePath"))
                .categoryId(rs.getInt("categoryId"))
                .productId(rs.getInt("productId"))
                .primary(rs.getInt("primaryImage"))
                .build(), productId);
    }
}

package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.repositories.CategoryRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    private static final String ADD_CATEGORY_QUERY = "INSERT INTO categories (name) VALUES (?)";
    private static final String GET_ALL_CATEGORIES_QUERY = "SELECT * FROM categories";
    private static final String UPDATE_CATEGORY_QUERY = "UPDATE categories SET name = ? WHERE id = ?";
    private static final String DELETE_CATEGORY_QUERY = "DELETE FROM categories WHERE id = ?";
    private static final String GET_CATEGORY_BY_ID_QUERY = "SELECT * FROM categories WHERE id = ?";

    @SneakyThrows(Exception.class)
    @Override
    public Category create(Category entity) {
        Connection connection = pool.getConnection();
        PreparedStatement psInsert = connection.prepareStatement(ADD_CATEGORY_QUERY);

        psInsert.setString(1, entity.getName());
        psInsert.execute();

        pool.closeConnection(connection);
        psInsert.close();

        return entity;
    }

    @SneakyThrows(Exception.class)
    @Override
    public List<Category> read() {
        List<Category> categories = new ArrayList<>();
        Connection connection = pool.getConnection();
        PreparedStatement psGet = connection.prepareStatement(GET_ALL_CATEGORIES_QUERY);

        ResultSet resultSet = psGet.executeQuery();
        while (resultSet.next()) {
            categories.add(Category.builder()
                    .id(resultSet.getInt(1))
                    .name(resultSet.getString(2))
                    .build()
            );
        }
        resultSet.close();

        pool.closeConnection(connection);
        psGet.close();
        return categories;
    }

    @SneakyThrows(Exception.class)
    @Override
    public Category update(Category entity) {
        Connection connection = pool.getConnection();
        PreparedStatement psUpdate = connection.prepareStatement(UPDATE_CATEGORY_QUERY);

        psUpdate.setInt(1, entity.getId());
        psUpdate.setString(2, entity.getName());
        psUpdate.execute();

        pool.closeConnection(connection);
        psUpdate.close();
        return entity;
    }

    @SneakyThrows(Exception.class)
    @Override
    public void delete(int id) {
        Connection connection = pool.getConnection();
        PreparedStatement psDelete = connection.prepareStatement(DELETE_CATEGORY_QUERY);

        psDelete.setInt(1, id);
        psDelete.execute();

        pool.closeConnection(connection);
        psDelete.close();
    }

    @SneakyThrows(Exception.class)
    @Override
    public Category findById(int id) {
        Category category = null;
        Connection connection = pool.getConnection();
        PreparedStatement psGet = connection.prepareStatement(GET_CATEGORY_BY_ID_QUERY);

        psGet.setInt(1, id);
        ResultSet resultSet = psGet.executeQuery();
        while (resultSet.next()) {
            category = Category.builder()
                    .id(resultSet.getInt(1))
                    .name(resultSet.getString(2))
                    .build();
        }
        resultSet.close();

        pool.closeConnection(connection);
        psGet.close();
        return category;
    }
}

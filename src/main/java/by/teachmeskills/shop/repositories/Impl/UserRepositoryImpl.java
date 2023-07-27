package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.enums.MapKeysEnum;
import by.teachmeskills.shop.repositories.UserRepository;
import by.teachmeskills.shop.utils.EncryptionUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static String updateQuery;
    private static final String ADD_USER_QUERY = "INSERT INTO users (name, surname, birthday, balance, email, password) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String GET_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String GET_USER_BY_EMAIL_AND_PASS_QUERY = "SELECT * FROM users WHERE email = ? AND password = ?";
    private static final Map<String, String> usersTableColumnNames = Map.of(
            MapKeysEnum.ID.getKey(), "id",
            MapKeysEnum.NAME.getKey(), "name",
            MapKeysEnum.SURNAME.getKey(), "surname",
            MapKeysEnum.BIRTHDAY.getKey(), "birthday",
            MapKeysEnum.BALANCE.getKey(), "balance",
            MapKeysEnum.EMAIL.getKey(), "email",
            MapKeysEnum.PASSWORD.getKey(), "password");
    @SneakyThrows(Exception.class)
    @Override
    public User create(User entity) {
        Connection connection = pool.getConnection();
        PreparedStatement psInsert = connection.prepareStatement(ADD_USER_QUERY);

        psInsert.setString(1, entity.getName());
        psInsert.setString(2, entity.getSurname());
        psInsert.setTimestamp(3, Timestamp.valueOf(entity.getBirthday().atStartOfDay()));
        psInsert.setBigDecimal(4, BigDecimal.valueOf(entity.getBalance()));
        psInsert.setString(5, entity.getEmail());
        psInsert.setString(6, EncryptionUtils.encrypt(entity.getPassword()));
        psInsert.execute();

        pool.closeConnection(connection);
        psInsert.close();
        return entity;
    }
    @SneakyThrows(Exception.class)
    @Override
    public List<User> read() {
        List<User> users = new ArrayList<>();
        Connection connection = pool.getConnection();
        PreparedStatement psGet = connection.prepareStatement(GET_ALL_USERS_QUERY);

        ResultSet resultSet = psGet.executeQuery();
        while (resultSet.next()) {
            users.add(User.builder()
                    .id(resultSet.getInt(1))
                    .name(resultSet.getString(2))
                    .surname(resultSet.getString(3))
                    .birthday(resultSet.getTimestamp(4).toLocalDateTime().toLocalDate())
                    .balance(resultSet.getBigDecimal(5).doubleValue())
                    .email(resultSet.getString(5))
                    .password(EncryptionUtils.decrypt(resultSet.getString(6)))
                    .build()
            );
        }

        resultSet.close();
        pool.closeConnection(connection);
        psGet.close();
        return users;
    }
    @SneakyThrows(Exception.class)
    @Override
    public User update(User entity) {
        try {
            Connection connection = pool.getConnection();
            PreparedStatement psUpdate = connection.prepareStatement(updateQuery);

            psUpdate.execute();

            pool.closeConnection(connection);
            psUpdate.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return entity;
    }
    @SneakyThrows(Exception.class)
    @Override
    public void delete(int id) {
        Connection connection = pool.getConnection();
        PreparedStatement psDelete = connection.prepareStatement(DELETE_USER_QUERY);

        psDelete.setInt(1, id);
        psDelete.execute();

        pool.closeConnection(connection);
        psDelete.close();
    }
    @SneakyThrows(Exception.class)
    @Override
    public User findById(int id) {
        User user = null;
        Connection connection = pool.getConnection();
        PreparedStatement psGet = connection.prepareStatement(GET_USER_BY_ID_QUERY);

        psGet.setInt(1, id);
        ResultSet resultSet = psGet.executeQuery();
        while (resultSet.next()) {
            user = User.builder()
                    .id(resultSet.getInt(1))
                    .name(resultSet.getString(2))
                    .surname(resultSet.getString(3))
                    .birthday(resultSet.getTimestamp(4).toLocalDateTime().toLocalDate())
                    .balance(resultSet.getBigDecimal(5).doubleValue())
                    .email(resultSet.getString(5))
                    .password(EncryptionUtils.decrypt(resultSet.getString(6)))
                    .build();
        }
        resultSet.close();

        pool.closeConnection(connection);
        psGet.close();
        return user;
    }
    @SneakyThrows(Exception.class)
    @Override
    public User findByEmailAndPassword(String email, String password) {
        User user = null;
        Connection connection = pool.getConnection();
        PreparedStatement psGet = connection.prepareStatement(GET_USER_BY_EMAIL_AND_PASS_QUERY);

        psGet.setString(1, email);
        psGet.setString(2, EncryptionUtils.encrypt(password));
        ResultSet resultSet = psGet.executeQuery();

        while (resultSet.next()) {
            user = User.builder()
                    .id(resultSet.getInt(1))
                    .name(resultSet.getString(2))
                    .surname(resultSet.getString(3))
                    .birthday(resultSet.getTimestamp(4).toLocalDateTime().toLocalDate())
                    .balance(resultSet.getBigDecimal(5).doubleValue())
                    .email(resultSet.getString(6))
                    .password(EncryptionUtils.decrypt(resultSet.getString(7)))
                    .build();
        }
        resultSet.close();

        pool.closeConnection(connection);
        psGet.close();
        return user;
    }

    @Override
    public void generateUpdateQuery(Map<String, String> userData, int userId) {
        StringBuilder query = new StringBuilder("UPDATE users SET ");

        if (userData.containsKey(MapKeysEnum.NEW_PASSWORD.getKey())) {
            updateQuery = query
                    .append(usersTableColumnNames.get(MapKeysEnum.PASSWORD.getKey()))
                    .append(" = '")
                    .append(EncryptionUtils.encrypt(userData.get(MapKeysEnum.NEW_PASSWORD.getKey())))
                    .append("' WHERE id = '")
                    .append(userId)
                    .append("'").toString();
            return;
        }

        for (Map.Entry<String, String> name : usersTableColumnNames.entrySet()) {
            if (userData.containsKey(name.getKey())) {
                query
                        .append(name.getKey())
                        .append(" = '")
                        .append(userData.get(name.getKey()))
                        .append("', ");
            }
        }

        query.deleteCharAt(query.lastIndexOf(","));
        updateQuery = query.append(" WHERE id = '").append(userId).append("'").toString();
    }
}

package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.enums.MapKeysEnum;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.repositories.UserRepository;
import by.teachmeskills.shop.utils.EncryptionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
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

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User entity) {
        return jdbcTemplate.execute(ADD_USER_QUERY, (PreparedStatementCallback<User>) ps -> {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getSurname());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getBirthday().atStartOfDay()));
            ps.setBigDecimal(4, BigDecimal.valueOf(entity.getBalance()));
            ps.setString(5, entity.getEmail());
            ps.setString(6, EncryptionUtils.encrypt(entity.getPassword()));
            ps.execute();

            return entity;
        });
    }

    @Override
    public List<User> read() {
        return jdbcTemplate.query(GET_ALL_USERS_QUERY, (rs, rowNum) -> User.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .surname(rs.getString("surname"))
                .birthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate())
                .balance(rs.getBigDecimal("balance").doubleValue())
                .email(rs.getString("email"))
                .password(EncryptionUtils.decrypt(rs.getString("password")))
                .build());
    }

    @Override
    public User update(User entity) {
        jdbcTemplate.update(updateQuery);
        return entity;
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_USER_QUERY, id);
    }

    @Override
    public User findById(int id) {
        return jdbcTemplate.queryForObject(GET_USER_BY_ID_QUERY, (RowMapper<User>) (rs, rowNum) -> User.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .surname(rs.getString("surname"))
                .birthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate())
                .balance(rs.getBigDecimal("balance").doubleValue())
                .email(rs.getString("email"))
                .password(EncryptionUtils.decrypt(rs.getString("password")))
                .build(), id);
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws EntityNotFoundException {
        try { return jdbcTemplate.queryForObject(GET_USER_BY_EMAIL_AND_PASS_QUERY, (RowMapper<User>) (rs, rowNum) -> User.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .surname(rs.getString("surname"))
                .email(rs.getString("email"))
                .password(EncryptionUtils.decrypt(rs.getString("password")))
                .birthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate())
                .balance(rs.getInt("balance"))
                .build(), email, EncryptionUtils.encrypt(password));}
        catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Пользователь не найден.");
        }
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

package by.teachmeskills.shop.repositories;

import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;

import java.util.Map;

public interface UserRepository extends BaseRepository<User> {
    User findById(int id);
    User findByEmailAndPassword(String email, String password) throws EntityNotFoundException;
    void generateUpdateQuery(Map<String, String> userData, int userId);
}

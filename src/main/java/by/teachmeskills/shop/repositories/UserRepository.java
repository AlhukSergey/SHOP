package by.teachmeskills.shop.repositories;

import by.teachmeskills.shop.domain.User;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;

public interface UserRepository extends BaseRepository<User> {
    User findById(int id);

    User findByEmailAndPassword(String email, String password) throws EntityNotFoundException;
}

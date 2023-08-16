package by.teachmeskills.shop.repositories;

import by.teachmeskills.shop.domain.User;

public interface UserRepository extends BaseRepository<User> {
    User findById(int id);
    User findByEmailAndPassword(String email, String password);
}

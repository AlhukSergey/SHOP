package by.teachmeskills.shop.repositories;

import by.teachmeskills.shop.domain.Category;

public interface CategoryRepository extends BaseRepository<Category> {
    Category findById(int id);
}

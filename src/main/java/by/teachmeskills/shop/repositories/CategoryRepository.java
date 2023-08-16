package by.teachmeskills.shop.repositories;

import by.teachmeskills.shop.domain.Category;

import java.util.List;

public interface CategoryRepository extends BaseRepository<Category> {
    Category findById(int id);

    List<Category> findPaginatedCategories(int first, int count);

    Long getTotalItems();
}
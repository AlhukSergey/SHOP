package by.teachmeskills.shop.repositories;

import by.teachmeskills.shop.domain.Product;

import java.util.List;

public interface ProductRepository extends BaseRepository<Product> {
    Product findById(int id);

    List<Product> findByCategoryId(int categoryId);
    List<Product> findBySearchParameter(String parameter);
}

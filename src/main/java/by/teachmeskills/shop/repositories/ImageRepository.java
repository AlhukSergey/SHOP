package by.teachmeskills.shop.repositories;

import by.teachmeskills.shop.domain.Image;

import java.util.List;

public interface ImageRepository extends BaseRepository<Image>{
    Image findById(int id);
    Image findByCategoryId(int categoryId);
    List<Image> findByProductId(int productId);
}

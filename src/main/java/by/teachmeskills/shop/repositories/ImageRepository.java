package by.teachmeskills.shop.repositories;

import by.teachmeskills.shop.domain.Image;

public interface ImageRepository extends BaseRepository<Image> {
    Image findById(int id);
}

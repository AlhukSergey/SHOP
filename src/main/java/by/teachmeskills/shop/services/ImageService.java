package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.Image;

import java.util.List;

public interface ImageService extends BaseService<Image> {
    Image getImageById(int id);
    Image getImageByCategoryId(int categoryId);
    List<Image> getImagesByProductId(int productId);
}

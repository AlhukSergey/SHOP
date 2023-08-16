package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;

import java.util.List;

public interface ImageService extends BaseService<Image> {
    Image getImageById(int id);
    Image getImageByCategoryId(int categoryId) throws EntityNotFoundException;
    List<Image> getImagesByProductId(int productId) throws EntityNotFoundException;
}

package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.repositories.ImageRepository;
import by.teachmeskills.shop.repositories.Impl.ImageRepositoryImpl;
import by.teachmeskills.shop.services.ImageService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository = new ImageRepositoryImpl();
    @Override
    public Image create(Image entity) {
        return imageRepository.create(entity);
    }

    @Override
    public List<Image> read() {
        return imageRepository.read();
    }

    @Override
    public Image update(Image entity) {
        return imageRepository.update(entity);
    }

    @Override
    public void delete(int id) {
        imageRepository.delete(id);
    }

    @Override
    public Image getImageById(int id) {
        return imageRepository.findById(id);
    }

    @Override
    public Image getImageByCategoryId(int categoryId) {
        return imageRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Image> getImagesByProductId(int productId) {
        return imageRepository.findByProductId(productId);
    }
}

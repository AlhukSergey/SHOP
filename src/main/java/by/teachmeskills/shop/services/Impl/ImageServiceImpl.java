package by.teachmeskills.shop.services.Impl;

import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;
import by.teachmeskills.shop.repositories.ImageRepository;
import by.teachmeskills.shop.services.ImageService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

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
    public Image getImageByCategoryId(int categoryId) throws EntityNotFoundException {
        Image image = imageRepository.findByCategoryId(categoryId);
        if(image == null) {
            throw new EntityNotFoundException("Изображение не найдено.");
        }
        return image;
    }

    @Override
    public List<Image> getImagesByProductId(int productId) throws EntityNotFoundException {
        List<Image> images = imageRepository.findByProductId(productId);
        if(images.isEmpty()) {
            throw new EntityNotFoundException("Изображение не найдено.");
        }
        return images;
    }
}

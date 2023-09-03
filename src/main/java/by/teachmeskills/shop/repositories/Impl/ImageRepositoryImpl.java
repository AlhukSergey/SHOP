package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.repositories.ImageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class ImageRepositoryImpl implements ImageRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Image create(Image entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public List<Image> read() {
        return entityManager.createQuery("select i from Image i ", Image.class).getResultList();
    }

    @Override
    public Image update(Image entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(int id) {
        Image image = Optional.ofNullable(entityManager.find(Image.class, id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Изображение с id %d не найдено.", id)));
        entityManager.remove(image);
    }

    @Override
    public Image findById(int id) {
        return entityManager.find(Image.class, id);
    }
}

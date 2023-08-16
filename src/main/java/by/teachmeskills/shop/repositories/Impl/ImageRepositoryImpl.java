package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Image;
import by.teachmeskills.shop.repositories.ImageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public class ImageRepositoryImpl implements ImageRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Image create(Image entity) {
        Session session = entityManager.unwrap(Session.class);
        session.persist(entity);
        return entity;
    }

    @Override
    public List<Image> read() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("select i from Image i ", Image.class).list();
    }

    @Override
    public Image update(Image entity) {
        Session session = entityManager.unwrap(Session.class);
        return session.merge(entity);
    }

    @Override
    public void delete(int id) {
        Session session = entityManager.unwrap(Session.class);
        Image image = session.get(Image.class, id);
        session.remove(image);
    }

    @Override
    public Image findById(int id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Image.class, id);
    }

    @Override
    public Image findByCategoryId(int categoryId) {
        Session session = entityManager.unwrap(Session.class);
        Query<Image> query = session.createQuery("select i from Image i where i.category.id=:category_id", Image.class);
        query.setParameter("category_id", categoryId);
        return query.uniqueResult();
    }

    @Override
    public List<Image> findByProductId(int productId) {
        Session session = entityManager.unwrap(Session.class);
        Query<Image> query = session.createQuery("select i from Image i where i.product.id=:product_id", Image.class);
        query.setParameter("product_id", productId);
        return query.list();
    }
}

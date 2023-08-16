package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.repositories.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Category create(Category entity) {
        Session session = entityManager.unwrap(Session.class);
        session.persist(entity);
        return entity;
    }

    @Override
    public List<Category> read() {
        Session session = entityManager.unwrap(Session.class);
        Query<Category> query = session.createQuery("from Category", Category.class);
        return query.list();
    }

    @Override
    public Category update(Category entity) {
        Session session = entityManager.unwrap(Session.class);
        return session.merge(entity);
    }

    @Override
    public void delete(int id) {
        Session session = entityManager.unwrap(Session.class);
        Category category = session.get(Category.class, id);
        session.remove(category);
    }

    @Override
    public Category findById(int id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Category.class, id);
    }

    @Override
    public List<Category> findPaginatedCategories(int first, int count) {
        Session session = entityManager.unwrap(Session.class);
        Query<Category> query = session.createQuery("from Category", Category.class);
        return query.setFirstResult(first).setMaxResults(count).list();
    }

    @Override
    public Long getTotalItems() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("select count(*) from Category", Long.class).getSingleResultOrNull();
    }
}

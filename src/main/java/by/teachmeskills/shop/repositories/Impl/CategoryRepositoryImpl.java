package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Category;
import by.teachmeskills.shop.repositories.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Category create(Category entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public List<Category> read() {
        return entityManager.createQuery("select c from Category c", Category.class).getResultList();
    }

    @Override
    public Category update(Category entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(int id) {
        Category category = entityManager.find(Category.class, id);
        entityManager.remove(category);
    }

    @Override
    public Category findById(int id) {
        return entityManager.find(Category.class, id);
    }

    @Override
    public List<Category> findPaginatedCategories(int first, int count) {
        return entityManager.createQuery("from Category", Category.class).setFirstResult(first).setMaxResults(count).getResultList();
    }

    @Override
    public Long getTotalItems() {
        return entityManager.createQuery("select count(*) from Category", Long.class).getSingleResult();
    }
}

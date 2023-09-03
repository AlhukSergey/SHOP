package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Statistic;
import by.teachmeskills.shop.repositories.StatisticRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Transactional
@Repository
public class StatisticRepositoryImpl extends StatisticRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Statistic create(Statistic entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public List<Statistic> read() {
        return entityManager.createQuery("select s from S s ", Statistic.class).getResultList();
    }

    @Override
    public void delete(int id) {
        Statistic statistic = entityManager.find(Statistic.class, id);
        entityManager.remove(statistic);
    }
}

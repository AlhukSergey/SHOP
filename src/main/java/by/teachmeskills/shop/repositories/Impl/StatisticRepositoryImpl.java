package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Statistic;
import by.teachmeskills.shop.repositories.StatisticRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
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
        Session session = entityManager.unwrap(Session.class);
        session.persist(entity);
        return entity;
    }

    @Override
    public List<Statistic> read() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("select s from S s ", Statistic.class).list();
    }

    @Override
    public Statistic update(Statistic entity) {
        Session session = entityManager.unwrap(Session.class);
        return session.merge(entity);
    }

    @Override
    public void delete(int id) {
        Session session = entityManager.unwrap(Session.class);
        Statistic statistic = session.get(Statistic.class, id);
        session.remove(statistic);
    }
}

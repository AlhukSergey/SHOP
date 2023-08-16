package by.teachmeskills.shop.repositories.Impl;

import by.teachmeskills.shop.domain.Order;
import by.teachmeskills.shop.repositories.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Order create(Order entity) {
        Session session = entityManager.unwrap(Session.class);
        session.persist(entity);
        return entity;
    }

    @Override
    public List<Order> read() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("select o Order o ", Order.class).list();
    }

    @Override
    public Order update(Order entity) {
        Session session = entityManager.unwrap(Session.class);
        return session.merge(entity);
    }

    @Override
    public void delete(int id) {
        Session session = entityManager.unwrap(Session.class);
        Order order = session.get(Order.class, id);
        session.remove(order);
    }

    @Override
    public Order findById(int id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Order.class, id);
    }

    @Override
    public List<Order> findByDate(LocalDateTime date) {
        Session session = entityManager.unwrap(Session.class);
        Query<Order> query = session.createQuery("select o from Order o where o.created_at=:created_at", Order.class);
        query.setParameter("created_at", Timestamp.valueOf(date));
        return query.list();

    }

    @Override
    public List<Order> findByUserId(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query<Order> query = session.createQuery("select o from Order o where o.user.id=:user_id", Order.class);
        query.setParameter("user_id", id);
        return query.list();
    }
}

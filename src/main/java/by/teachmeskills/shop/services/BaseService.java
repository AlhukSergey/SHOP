package by.teachmeskills.shop.services;

import by.teachmeskills.shop.domain.BaseEntity;
import by.teachmeskills.shop.exceptions.EntityNotFoundException;

public interface BaseService<T extends BaseEntity> {
    T create(T entity);

    void read();

    T update(T entity);

    void delete(int id) throws EntityNotFoundException;
}

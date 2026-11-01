package com.campus.lostfound.dao;

import com.campus.lostfound.exception.DatabaseException;
import java.util.List;
import java.util.Optional;

/** GENERIC interface (Week 14): T = the entity type, ID = the type of its primary key. */
public interface Repository<T, ID> {
    T save(T entity) throws DatabaseException;
    Optional<T> findById(ID id) throws DatabaseException;
    List<T> findAll() throws DatabaseException;
}

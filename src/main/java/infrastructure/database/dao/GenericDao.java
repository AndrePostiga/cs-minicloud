package infrastructure.database.dao;

import java.io.Serializable;

public interface GenericDao<T, PK extends Serializable> {
    T Create(T obj);

    void Update(T obj);

    void Delete(T obj);

    T GetById(PK id);

    T GetByIdWithLock(PK id);
}

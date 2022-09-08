package infrastructure.database.dao;

import java.util.List;

public interface GenericDao<T> {
    List<T> getAll();
    T getById(Long id);
    T create(T type);
    Boolean delete(Long id);
    T update(Long id, T type);
}

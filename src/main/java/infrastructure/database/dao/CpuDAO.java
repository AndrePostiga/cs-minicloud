package infrastructure.database.dao;

import domain.MachineAggregate.Entities.CPU;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CpuDAO {

    @PersistenceContext
    private EntityManager em;

    public List<CPU> getAll() {
        List<CPU> list = em.createQuery("SELECT t FROM " + CPU.class.getSimpleName() + " t", CPU.class).getResultList();
        return list;
    }

    public CPU getById(Long id) {
        CPU recoveredEntity = em.find(CPU.class, id);
        return recoveredEntity;
    }

    public CPU create(CPU type) {
        em.persist(type);
        return type;
    }

    public Boolean delete(Long id) {
        CPU entityType = em.find(CPU.class, id);
        if (entityType == null) {
            throw new EntityNotFoundException(CPU.class.getSimpleName() + " solicitado não foi encontrado");
        }

        em.remove(entityType);
        return true;
    }

    public CPU update(Long id, CPU type) {
        CPU entityType = em.find(CPU.class, id, LockModeType.PESSIMISTIC_WRITE);
        if (entityType == null) {
            throw new EntityNotFoundException(CPU.class.getSimpleName() + " solicitado não foi encontrado");
        }

        em.merge(entityType);
        return entityType;
    }
}

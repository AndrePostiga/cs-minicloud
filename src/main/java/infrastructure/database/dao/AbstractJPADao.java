package infrastructure.database.dao;

import infrastructure.database.EntityManagerSingletonFactory;
import javassist.NotFoundException;

import javax.persistence.*;
import javax.transaction.Transaction;
import java.util.List;

public abstract class AbstractJPADao<T> implements GenericDao<T> {

    Class<T> entity;

    public AbstractJPADao(Class<T> entity) {
        this.entity = entity;
    }

    @Override
    public List<T> getAll() {
        EntityManager em = EntityManagerSingletonFactory.getInstance();
        List<T> list = em.createQuery("SELECT t FROM " + entity.getSimpleName() + " t", entity).getResultList();
        em.close();
        return list;
    }

    @Override
    public T getById(Long id) {
        EntityManager em = EntityManagerSingletonFactory.getInstance();
        T recoveredEntity = em.find(entity, id);
        em.close();
        return recoveredEntity;
    }

    @Override
    public T create(T type) {
        EntityManager em = EntityManagerSingletonFactory.getInstance();
        try {
            em.getTransaction().begin();
            em.persist(type);
            em.getTransaction().commit();
            return type;
        }
        catch (RuntimeException ex) {
            if (em.getTransaction() != null) {
                try {
                    em.getTransaction().rollback();
                } catch (RuntimeException he) {}
            }
            throw ex;
        }
        finally {
            em.close();
        }
    }

    @Override
    public Boolean delete(Long id) {
        EntityManager em = EntityManagerSingletonFactory.getInstance();

        try {
            em.getTransaction().begin();

            T entityType = em.find(entity, id);

            if (entityType == null) {
                em.getTransaction().rollback();
                return false;
                //throw new EntityNotFoundException(entity.getSimpleName() + " solicitado não foi encontrado");
            }

            em.remove(entityType);
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            if (em.getTransaction() != null) {
                try {
                    em.getTransaction().rollback();
                } catch (RuntimeException he) {
                }
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public T update(Long id, T type) {
        EntityManager em = EntityManagerSingletonFactory.getInstance();
        T entityType = null;

        try {
            em.getTransaction().begin();
            entityType = em.find(entity, id, LockModeType.PESSIMISTIC_WRITE);

            if (entityType == null) {
                em.getTransaction().rollback();
                return null;
                //throw new EntityNotFoundException(entity.getSimpleName() + " solicitado não foi encontrado");
            }

            em.merge(type);
            em.getTransaction().commit();
        }
        catch (OptimisticLockException e){
            if (em.getTransaction() != null) em.getTransaction().rollback();
            throw e;
        }
        catch (RuntimeException e) {
            if (em.getTransaction() != null) {
                try {
                    em.getTransaction().rollback();
                } catch (RuntimeException he) {
                }
            }
            throw e;
        }
        finally {
            em.close();
        }

        return entityType;
    }
}

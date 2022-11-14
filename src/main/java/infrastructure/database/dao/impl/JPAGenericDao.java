package infrastructure.database.dao.impl;

import infrastructure.database.dao.GenericDao;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class JPAGenericDao<T, PK extends Serializable> implements GenericDao<T, PK> {

    private Class<T> tipo;

    @PersistenceContext
    protected EntityManager em;

    public JPAGenericDao(Class<T> tipo) {
        this.tipo = tipo;
    }

    @Override
    public final T Create(T obj) {
        em.persist(obj);
        return obj;
    }

    @Override
    public final void Update(T obj) {
        em.merge(obj);
    }

    @Override
    public final void Delete(T obj) {
        em.remove(obj);
    }

    @Override
    public final T GetById(PK id) {
        return em.find(tipo, id);
    }

    @Override
    public final T GetByIdWithLock(PK id) {
        return em.find(tipo, id, LockModeType.PESSIMISTIC_WRITE);
    }

    public final T busca(Method metodo, Object[] argumentos) {
        T t = null;

        String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
        Query namedQuery = em.createNamedQuery(nomeDaBusca);

        if (argumentos != null) {
            for (int i = 0; i < argumentos.length; i++) {
                Object arg = argumentos[i];
                namedQuery.setParameter(i + 1, arg); // Parâmetros de buscas são 1-based.
            }
        }
        t = (T) namedQuery.getSingleResult();

        return t;
    }

    public final T buscaUltimoOuPrimeiro(Method metodo, Object[] argumentos) {
        List<T> lista;
        String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
        Query namedQuery = em.createNamedQuery(nomeDaBusca);

        if (argumentos != null) {
            for (int i = 0; i < argumentos.length; i++) {
                Object arg = argumentos[i];
                namedQuery.setParameter(i + 1, arg);
            }
        }
        lista = namedQuery.getResultList();

        T t = (lista.size() == 0) ? null : lista.get(0);
        return t;
    }

    public final List<T> buscaLista(Method metodo, Object[] argumentos) {
        String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
        Query namedQuery = em.createNamedQuery(nomeDaBusca);

        if (argumentos != null) {
            for (int i = 0; i < argumentos.length; i++) {
                Object arg = argumentos[i];
                namedQuery.setParameter(i + 1, arg); // Parâmetros de buscas são 1-based.
            }
        }
        return (List<T>) namedQuery.getResultList();
    }

    public final Set<T> buscaConjunto(Method metodo, Object[] argumentos) {
        String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
        Query namedQuery = em.createNamedQuery(nomeDaBusca);

        if (argumentos != null) {
            for (int i = 0; i < argumentos.length; i++) {
                Object arg = argumentos[i];
                namedQuery.setParameter(i + 1, arg); // Parâmetros de buscas são 1-based.
            }
        }

        List<T> lista = namedQuery.getResultList();

        return new LinkedHashSet<T>(lista);
    }

    private String getNomeDaBuscaPeloMetodo(Method metodo) {
        return tipo.getSimpleName() + "." + metodo.getName();
    }
}

package infrastructure.database.dao.controle;

import annotation.RecuperaConjunto;
import annotation.RecuperaLista;
import annotation.RecuperaObjeto;
import annotation.RecuperaUltimoOuPrimeiro;
import infrastructure.database.dao.impl.JPAGenericDao;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class DaoIntercept implements MethodInterceptor  {
    public Object intercept(Object objeto, Method metodo, Object[] args, MethodProxy metodoProxy) throws Throwable {

        JPAGenericDao<?, ?> daoGenerico = (JPAGenericDao<?, ?>) objeto;

        if (metodo.isAnnotationPresent(RecuperaLista.class)) {
            return daoGenerico.buscaLista(metodo, args);
        } else if (metodo.isAnnotationPresent(RecuperaConjunto.class)) {
            return daoGenerico.buscaConjunto(metodo, args);
        } else if (metodo.isAnnotationPresent(RecuperaUltimoOuPrimeiro.class)) {
            return daoGenerico.buscaUltimoOuPrimeiro(metodo, args);
        } else if (metodo.isAnnotationPresent(RecuperaObjeto.class)) {
            return daoGenerico.busca(metodo, args);
        } else {
            throw new Exception(
                    "O método " + metodo.getName() + " da classe " + metodo.getDeclaringClass() + " não foi anotado");
        }
    }
}

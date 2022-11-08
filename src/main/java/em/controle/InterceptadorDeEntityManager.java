package em.controle;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import servico.controle.JPAUtil;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;

public class InterceptadorDeEntityManager implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        try {
            Object result = method.invoke(JPAUtil.getEntityManager(), objects);
            System.out.println("<<<<<<<<< Atualizou o EntityManager pra thread local >>>>>>>>>");
            return result;
        }
        catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            throw e;
        }
    }
}

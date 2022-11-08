package dao.controle;

import anotacao.PersistenceContent;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import servico.controle.JPAUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InterceptadorDeDAO implements MethodInterceptor {

    private Class<?> classeInterceptada;

    public InterceptadorDeDAO(Class<?> classe) {
        this.classeInterceptada = classe;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        try {
            System.out.println(">>>>>>>>> Começou Interceptação de método <<<<<<<<<");
            Field[] campos = this.classeInterceptada.getDeclaredFields();
            for (Field campo : campos) {
                if (campo.isAnnotationPresent(PersistenceContent.class)) {
                    campo.setAccessible(true);
                    try {
                        campo.set(o, JPAUtil.getEntityManager());
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            Object obj = methodProxy.invokeSuper(o, objects);
            System.out.println(">>>>>>>>> Terminou Interceptação de método <<<<<<<<<");
            return obj;
        }
        catch (Exception e) {
            throw e;
        }
    }
}

package dao.controle;

import anotacao.PersistenceContent;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import servico.controle.JPAUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InterceptadorDeDAO implements MethodInterceptor {

    private Class<?> classeInterceptada;
    private Field campo;

    public InterceptadorDeDAO(Class<?> classe) {
        this.classeInterceptada = classe;
        Field[] campos = this.classeInterceptada.getDeclaredFields();
        for (Field campo : campos) {
            if (campo.isAnnotationPresent(PersistenceContent.class)) {
                this.campo = campo;
            }
        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        try {
            System.out.println(">>>>>>>>> Começou Interceptação de método <<<<<<<<<");
            campo.setAccessible(true);
            campo.set(o, JPAUtil.getEntityManager());
            Object obj = methodProxy.invokeSuper(o, objects);
            System.out.println(">>>>>>>>> Terminou Interceptação de método <<<<<<<<<");
            return obj;
        }
        catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            throw e;
        }
    }
}

package servico.controle;

import anotacao.Transactional;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class InterceptadorDeServico implements MethodInterceptor {
	/*
	 * Parametros:
	 * 
	 * objeto - "this", o objeto "enhanced", isto é, o proxy.
	 * 
	 * metodo - o método interceptado, isto é, um método da interface ProdutoDAO,
	 * LanceDAO, etc.
	 * 
	 * args - um array de args; tipos primitivos são empacotados. Contém os
	 * argumentos que o método interceptado recebeu.
	 * 
	 * metodoProxy - utilizado para executar um método super. Veja o comentário
	 * abaixo.
	 * 
	 * MethodProxy - Classes geradas pela classe Enhancer passam este objeto para o
	 * objeto MethodInterceptor registrado quando um método interceptado é
	 * executado. Ele pode ser utilizado para invocar o método original, ou chamar o
	 * mesmo método sobre um objeto diferente do mesmo tipo.
	 * 
	 */

	public Object intercept(Object objeto, Method metodo, Object[] args, MethodProxy metodoOriginal) throws Throwable {
		try {
			if (metodo.isAnnotationPresent(Transactional.class)) {
				JPAUtil.beginTransaction();
			}

			System.out.println(
					"Método interceptado: " + metodo.getName() + " da classe " + metodo.getDeclaringClass().getName());
			// metodo.invoke(objeto, args);

			Object obj = metodoOriginal.invokeSuper(objeto, args);

			if (metodo.isAnnotationPresent(Transactional.class)) {
				JPAUtil.commitTransaction();
			}

			return obj;
		} catch (RuntimeException e) {
			if (metodo.isAnnotationPresent(Transactional.class)) {
				JPAUtil.rollbackTransaction();
			}
			throw e;
		} catch (Exception e) {
			if (metodo.isAnnotationPresent(Transactional.class)) {
				Class<?>[] classes = metodo.getAnnotation(Transactional.class).rollbackFor();
				boolean achou = false;
				for (Class<?> classe : classes) {
					if (classe.isInstance(e)) {
						achou = true;
						break;
					}
				}
				if (achou) {
					JPAUtil.rollbackTransaction();
				} else {
					JPAUtil.commitTransaction();
				}
			}
			throw e;
		} finally {
			JPAUtil.closeEntityManager();
		}
	}
}

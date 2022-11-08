package dao.controle;
import net.sf.cglib.proxy.Enhancer;
import org.reflections.Reflections;
import java.util.Set;

public class FabricaDeDAOs {

	@SuppressWarnings("unchecked")
	public static <T> T getDAO(Class<T> tipo) {

		Reflections reflections = new Reflections("dao.impl");
		Set<Class<? extends T>> classes = reflections.getSubTypesOf(tipo);

		if (classes.size() > 1)
			throw new RuntimeException("Somente uma classe pode implementar " + tipo.getName());

		Class<?> classe = classes.iterator().next();
		System.out.println(">>>>>>>>> Montou o Proxy da Classe <<<<<<<<<");

		T dao = (T) Enhancer.create(classe, new InterceptadorDeDAO(classe));
//		T dao = (T) Enhancer.create(classe, new MethodInterceptor() {
//			@Override
//			public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//				try {
//					System.out.println(">>>>>>>>> Começou Interceptação de método <<<<<<<<<");
//					Field[] campos = classe.getDeclaredFields();
//					for (Field campo : campos) {
//						if (campo.isAnnotationPresent(PersistenceContent.class)) {
//							campo.setAccessible(true);
//							try {
//								campo.set(o, JPAUtil.getEntityManager());
//							} catch (IllegalArgumentException | IllegalAccessException e) {
//								throw new RuntimeException(e);
//							}
//						}
//					}
//
//					Object obj = methodProxy.invokeSuper(o, objects);
//					System.out.println(">>>>>>>>> Terminou Interceptação de método <<<<<<<<<");
//					return obj;
//				}
//				catch (Exception e) {
//					throw e;
//				}
//			}
//		});

		return dao;
	}
}

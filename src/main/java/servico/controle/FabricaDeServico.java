package servico.controle;

import anotacao.Autowired;
import dao.controle.FabricaDeDAOs;
import net.sf.cglib.proxy.Enhancer;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Set;

public class FabricaDeServico {
//    private static ResourceBundle prop;
//
//    static {
//	try {
//	    prop = ResourceBundle.getBundle("servico");
//	}
//	catch (MissingResourceException e) {
//	    System.out.println("Aquivo servico.properties não encontrado.");
//	    throw new RuntimeException(e);
//	}
//    }

	// Esse método pode ser executado de 2 formas:
	// 1. produtoAppService =
	// FabricaDeServico.<ProdutoAppService>getServico(ProdutoAppService.class);
	// 2. produtoAppService = FabricaDeServico.getServico(ProdutoAppService.class);

	@SuppressWarnings("unchecked")
	public static <T> T getServico(Class<T> tipo) {

		Reflections reflections = new Reflections("servico.impl");

		Set<Class<? extends T>> classes = reflections.getSubTypesOf(tipo);

		if (classes.size() > 1)
			throw new RuntimeException("Somente uma classe pode implementar " + tipo.getName());

		Class<?> classe = classes.iterator().next();

		T servico = (T) Enhancer.create(classe, new InterceptadorDeServico());

		Field[] campos = classe.getDeclaredFields();
		for (Field campo : campos) {
			if (campo.isAnnotationPresent(Autowired.class)) {
				campo.setAccessible(true);
				try {
					campo.set(servico, FabricaDeDAOs.getDAO(campo.getType()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return servico;

		// O proxy deve estender a classe (ProdutoAppService por exemplo).
		// O proxy deve ainda chamar o método intercept() da classe interceptadora,
		// isto é, da classe InterceptadorDeServico (classe callback).

		// Enhancer enhancer = new Enhancer();
		// enhancer.setSuperclass(classeDoServico); // Superclasse do Servico
		// enhancer.setCallback(new InterceptadorDeServico()); // Interceptador do
		// Servico

		// return (T) enhancer.create();
	}
}
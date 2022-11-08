package em.controle;
import net.sf.cglib.proxy.Enhancer;
import javax.persistence.EntityManager;

public class FabricaDeEntityManagers {

    @SuppressWarnings("unchecked")
    public static EntityManager getEntityManager() {
        System.out.println(">>>>>>>>> Montou o Proxy do EntityManager <<<<<<<<<");
        return (EntityManager) Enhancer.create(EntityManager.class, new InterceptadorDeEntityManager());
    }

}

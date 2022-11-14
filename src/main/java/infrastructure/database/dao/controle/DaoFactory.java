package infrastructure.database.dao.controle;
import infrastructure.database.dao.impl.CPUDaoImpl;
import infrastructure.database.dao.impl.PhysicalMachineDaoImpl;
import infrastructure.database.dao.impl.VirtualMachineDaoImpl;
import net.sf.cglib.proxy.Enhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public static CPUDaoImpl GetCpuDao() throws Exception {
        return getDao(infrastructure.database.dao.impl.CPUDaoImpl.class);
    }

    @Bean
    public static PhysicalMachineDaoImpl GetPhysicalMachineDao() throws Exception {
        return getDao(infrastructure.database.dao.impl.PhysicalMachineDaoImpl.class);
    }

    @Bean
    public static VirtualMachineDaoImpl GetVirtualMachineDao() throws Exception {
        return getDao(infrastructure.database.dao.impl.VirtualMachineDaoImpl.class);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getDao(Class<T> classeDao) throws Exception {
        return (T) Enhancer.create(classeDao, new DaoIntercept());
    }
}

package infrastructure.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class EntityManagerSingletonFactory {

    private static final ResourceBundle properties;

    static {
        try {
            properties = ResourceBundle.getBundle("database");
        }
        catch (MissingResourceException ex) {
            System.out.println("Aquivo database.properties não encontrado.");
            throw new RuntimeException(ex);
        }
    }

    private static EntityManagerSingletonFactory emInstance;
    private EntityManagerFactory emFactory;

    private EntityManagerSingletonFactory(String persistenceUnit) {
        try {
            emFactory = Persistence.createEntityManagerFactory(persistenceUnit);
        }
        catch (Throwable e) {
            e.printStackTrace();
            System.out.println(">>>>>>>>>> Mensagem de erro: " + e.getMessage());
        }
    }

    public static EntityManager getInstance()
    {
        if (emInstance == null)
            emInstance = new EntityManagerSingletonFactory(properties.getString("database-persistence-unit"));

        return emInstance.emFactory.createEntityManager();
    }
}

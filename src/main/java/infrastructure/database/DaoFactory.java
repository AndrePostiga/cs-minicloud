package infrastructure.database;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DaoFactory {

    private static final ResourceBundle properties;

    static {
        try {
            properties = ResourceBundle.getBundle("dao");
        }
        catch (MissingResourceException ex) {
            System.out.println("Aquivo dao.properties n�o encontrado.");
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getDAO(Class<T> type) {
        T dao;
        String className = null;

        try {
            className = properties.getString(type.getSimpleName());
            dao = (T) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            System.out.println("N�o foi poss�vel criar um objeto do type " + className);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            System.out.println("N�o foi poss�vel criar um objeto do type " + className);
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.out.println("Classe " + className + " N�o encontrada");
            throw new RuntimeException(e);
        } catch (MissingResourceException e) {
            System.out.println("Chave " + type + " N�o encontrada em dao.properties");
            throw new RuntimeException(e);
        }

        return dao;
    }
}

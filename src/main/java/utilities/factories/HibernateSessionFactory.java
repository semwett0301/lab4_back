package utilities.factories;

import entities.Point;
import entities.User;
import exceptions.NoDataWasReceivedException;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

@NoArgsConstructor
public class HibernateSessionFactory {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() throws NoDataWasReceivedException {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Point.class);
                configuration.addAnnotatedClass(User.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                sessionFactory = null;
                throw new NoDataWasReceivedException();
             }
        }
        return sessionFactory;
    }

}

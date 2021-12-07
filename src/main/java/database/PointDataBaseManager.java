package database;

import entities.Point;
import exceptions.DataNotUpdateException;
import exceptions.NoDataWasReceivedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utilities.factories.HibernateSessionFactory;

import java.util.List;
import java.util.Optional;

public class PointDataBaseManager implements PointDAO{
    SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();

    @Override
    public void add(Point point) throws DataNotUpdateException {
        if (sessionFactory != null) {
            try {
                Session session = sessionFactory.openSession();
                session.save(point);
                session.close();
            } catch(Exception e) {
                throw new DataNotUpdateException();
            }
        }
        throw new DataNotUpdateException();
    }

    @Override
    public void clear(String username) throws DataNotUpdateException {
        if (sessionFactory != null) {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            try {
                Query query = session.createNativeQuery("DELETE FROM Point WHERE username = :username");
                query.setParameter("username", username);

                query.executeUpdate();
                transaction.commit();
                session.close();
            } catch (Exception e) {
                throw new DataNotUpdateException();
            }
        }
        throw new DataNotUpdateException();
    }

    @Override
    public List<Point> findAllPoints(String username) throws NoDataWasReceivedException {
        if (sessionFactory != null) {
            SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
            Session session = sessionFactory.openSession();
            try {
                Query<Point> query = session.createQuery("FROM Point WHERE username =:username");
                query.setParameter("username", username);
                return query.list();
            } catch (Exception e) {
                throw new NoDataWasReceivedException();
            }
        }
        throw new NoDataWasReceivedException();
    }
}

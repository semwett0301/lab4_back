package database.pointdao;

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
    private SessionFactory sessionFactory;

    {
        try {
            sessionFactory = HibernateSessionFactory.getSessionFactory();
        } catch (NoDataWasReceivedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Point point) throws DataNotUpdateException {
        if (sessionFactory != null) {
            try {
                Session session = sessionFactory.openSession();
                System.out.println("Lox");
                session.save(point);
                session.close();
                return;
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
                Query query = session.createQuery("DELETE FROM Point WHERE username = :username");
                query.setParameter("username", username);

                query.executeUpdate();
                transaction.commit();
                session.close();
                return;
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
                Query<Point> query = session.createQuery("FROM Point WHERE username = :username");
                query.setParameter("username", username);
                return query.list();
            } catch (Exception e) {
                throw new NoDataWasReceivedException();
            }
        }
        throw new NoDataWasReceivedException();
    }
}

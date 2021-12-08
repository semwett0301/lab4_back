package database.userdao;

import entities.Point;
import entities.User;
import exceptions.DataNotUpdateException;
import exceptions.NoDataWasReceivedException;
import exceptions.UserAlreadyExistException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utilities.factories.HibernateSessionFactory;

import java.util.List;

public class UserDataBaseManager implements UserDAO{
    private SessionFactory sessionFactory;

    {
        try {
            sessionFactory = HibernateSessionFactory.getSessionFactory();
        } catch (NoDataWasReceivedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(User user) throws DataNotUpdateException, UserAlreadyExistException {
        if (sessionFactory != null) {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("FROM User WHERE username = :username");
            query.setParameter("username", user.getUsername());
            List<Point> userList = query.list();
            if (userList.isEmpty()) {
                session.save(user);
            } else {
                throw new UserAlreadyExistException();
            }
            transaction.commit();
            session.close();
            return;
        }

        throw new DataNotUpdateException();
    }

    @Override
    public boolean checkUserPassword(String username, String password) throws NoDataWasReceivedException {
        User verifiableUser = getUser(username);
        if (username != null) {
            if (password.equals(verifiableUser.getPassword())) return true;
            return false;
        }
        throw new NoDataWasReceivedException();
    }

    @Override
    public boolean checkUserExist(String username) throws NoDataWasReceivedException {
        if (sessionFactory != null) {
            try {
                Session session = sessionFactory.openSession();
                Query query = session.createQuery("FROM User WHERE username = :username");
                query.setParameter("username", username);
                if (query.list().isEmpty()) return false;
                return true;
            } catch (Exception e) {
                throw new NoDataWasReceivedException();
            }
        }
        throw new NoDataWasReceivedException();
    }

    private User getUser(String username) throws NoDataWasReceivedException {
        if (sessionFactory != null) {
            try {
                Session session = sessionFactory.openSession();
                Query query = session.createQuery("FROM User WHERE username = :username");
                query.setParameter("username", username);
                return (User) query.getSingleResult();
            } catch (Exception e) {
                throw new NoDataWasReceivedException();
            }
        }
        throw new NoDataWasReceivedException();
    }
}

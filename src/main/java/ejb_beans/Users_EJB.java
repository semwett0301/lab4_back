package ejb_beans;

import database.userdao.UserDAO;
import database.userdao.UserDataBaseManager;
import entities.User;
import exceptions.DataNotUpdateException;
import exceptions.NoDataWasReceivedException;

import exceptions.UserAlreadyExistException;

import javax.ejb.Stateless;
import java.io.Serializable;


@Stateless
public class Users_EJB implements Serializable {
    UserDAO userDAO = new UserDataBaseManager();

    public void add(String username, String password) throws DataNotUpdateException, UserAlreadyExistException {
        User newUser = new User(username, password);
        userDAO.add(newUser);
    }

    public boolean checkUserExist(String username) throws NoDataWasReceivedException {
        return userDAO.checkUserExist(username);
    }

    public boolean checkUserPassword(String username, String password) throws NoDataWasReceivedException {
        return userDAO.checkUserPassword(username, password);
    }
}

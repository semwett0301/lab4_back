package ejb_beans;

import database.UserDAO;
import database.UserDataBaseManager;
import entities.User;
import exceptions.DataNotUpdateException;
import exceptions.NoDataWasReceivedException;

import exceptions.UserAlreadyExistException;




public class Users_EJB {
    UserDAO userDAO = new UserDataBaseManager();

    public void add(String username, String password) throws DataNotUpdateException, UserAlreadyExistException {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        userDAO.add(newUser);
    }

    public boolean checkUserExist(String username) throws NoDataWasReceivedException {
        return userDAO.checkUserExist(username);
    }

    public boolean checkUserPassword(String username, String password) throws NoDataWasReceivedException {
        return userDAO.checkUserPassword(username, password);
    }
}

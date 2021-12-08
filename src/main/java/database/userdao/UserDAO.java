package database.userdao;

import entities.User;
import exceptions.DataNotUpdateException;
import exceptions.NoDataWasReceivedException;
import exceptions.UserAlreadyExistException;


public interface UserDAO {
    void add(User user) throws DataNotUpdateException, UserAlreadyExistException;
    boolean checkUserPassword(String username, String password) throws NoDataWasReceivedException;
    boolean checkUserExist(String username) throws NoDataWasReceivedException;
}

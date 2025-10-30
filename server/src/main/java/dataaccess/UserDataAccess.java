package dataaccess;

import model.UserData;

import java.sql.SQLException;

public interface UserDataAccess {

    void addUser(UserData userData) throws SQLException, DataAccessException;

    UserData getUser(String username) throws SQLException, DataAccessException;

    void clear();

}

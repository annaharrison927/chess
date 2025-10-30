package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public interface AuthDataAccess {

    void addAuth(AuthData authData) throws DataAccessException, SQLException;

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);

    void clear();
}

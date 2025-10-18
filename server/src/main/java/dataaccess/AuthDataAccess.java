package dataaccess;

import model.AuthData;

public interface AuthDataAccess {

    void addAuth(AuthData authData);

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);

    void clear();
}

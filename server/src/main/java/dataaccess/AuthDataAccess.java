package dataaccess;

import model.AuthData;

public interface AuthDataAccess {

    void addAuth(AuthData authData);

    AuthData getAuth(AuthData authData);

    void deleteAuth(AuthData authData);
}

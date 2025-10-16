package dataaccess;

import model.AuthData;

public interface AuthDataAccess {

    void addAuth(AuthData authData);

    void getAuth(AuthData authData);

    void deleteAuth(AuthData authData);
}

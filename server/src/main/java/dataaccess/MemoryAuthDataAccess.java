package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDataAccess implements AuthDataAccess {
    private HashMap<String, AuthData> auth = new HashMap<>();

    @Override
    public void addAuth(AuthData authData) {
        auth.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(AuthData authData) {
        return auth.get(authData.authToken());
    }

    @Override
    public void deleteAuth(AuthData authData) {
        auth.remove(authData.authToken());
    }
}

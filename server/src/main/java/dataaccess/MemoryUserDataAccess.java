package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDataAccess implements UserDataAccess {
    private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void addUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    @Override
    public void getUser(UserData userData) {
        users.get(userData.username());
    }

    @Override
    public void clear() {
        users.clear();
    }
}

package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.util.HashMap;

public class MemoryUserDataAccess implements UserDataAccess {
    private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void addUser(UserData userData) throws SQLException {
        users.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void clear() {
        users.clear();
    }
}

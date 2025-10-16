package dataaccess;

import model.UserData;

public interface UserDataAccess {

    void addUser(UserData userData);

    UserData getUser(UserData userData);

    void clear();

}

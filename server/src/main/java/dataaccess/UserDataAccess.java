package dataaccess;

import model.UserData;

public interface UserDataAccess {

    void clear();

    void addUser(UserData userData);

    void getUser(UserData userData);

}

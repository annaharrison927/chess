package dataaccess;

import model.UserData;

public interface UserDataAccess {

    void addUser(UserData userData);

    void getUser(UserData userData);

    void clear();

}

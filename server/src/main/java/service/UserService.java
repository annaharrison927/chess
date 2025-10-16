package service;

import record.RegisterResult;
import request.RegisterRequest;
import dataaccess.UserDataAccess;
import model.UserData;
import dataaccess.DataAccessException;

public class UserService {
    private final UserDataAccess userDataAccess;
    private final UserData userData;

    public UserService(UserDataAccess userDataAccess, UserData userData) {
        this.userDataAccess = userDataAccess;
        this.userData = userData;
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        if (userDataAccess.getUser(userData.username) == null) {
            // Where am I getting userData from? From MemoryUserDataAccess or UserData?
        }
    }
}

package service;

import dataaccess.MemoryUserDataAccess;
import record.RegisterResult;
import request.RegisterRequest;
import dataaccess.UserDataAccess;
import model.UserData;

public class Service {
    private final UserDataAccess userDataAccess;


    public Service() {
        userDataAccess = new MemoryUserDataAccess();
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        if (userDataAccess.getUser(newUser) != null) {
        }
        userDataAccess.addUser(newUser);

    }
}

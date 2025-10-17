package service;

import dataaccess.AuthDataAccess;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryUserDataAccess;
import record.RegisterResult;
import request.RegisterRequest;
import dataaccess.UserDataAccess;
import model.UserData;
import model.AuthData;

import java.util.UUID;

public class Service {
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;


    public Service() {
        userDataAccess = new MemoryUserDataAccess();
        authDataAccess = new MemoryAuthDataAccess();
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        // Create new user data
        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        // Check if user is already in the database. If not, add the new user to database
        if (userDataAccess.getUser(newUser) != null) {
            // Throw Already Taken Exception
        }
        userDataAccess.addUser(newUser);

        // Generate new authToken and add to database
        String authToken = generateToken();
        AuthData newAuth = new AuthData(authToken, newUser.username());
        authDataAccess.addAuth(newAuth);

        // Create register result
        return new RegisterResult(newUser.username(), authToken);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}

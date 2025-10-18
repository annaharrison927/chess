package service;

import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryUserDataAccess;
import dataaccess.MemoryGameDataAccess;
import request.ClearApplicationRequest;
import result.ClearApplicationResult;
import result.RegisterResult;
import request.RegisterRequest;
import model.UserData;
import model.AuthData;

import java.util.Objects;
import java.util.UUID;

public class Service {
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;


    public Service() {
        userDataAccess = new MemoryUserDataAccess();
        authDataAccess = new MemoryAuthDataAccess();
        gameDataAccess = new MemoryGameDataAccess();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTakenException, BadRequestException {
        // Create new user data
        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        // Check if user is already in the database. If not, add the new user to database
        if (userDataAccess.getUser(newUser) != null) {
            throw new AlreadyTakenException("Error: User already in database");
        } else if (Objects.equals(newUser.username(), "")) {
            throw new BadRequestException("Error: Username is blank");
        } else if (Objects.equals(newUser.password(), null)) {
            throw new BadRequestException("Error: Please enter a password");
        } else if (Objects.equals(newUser.email(), null)) {
            throw new BadRequestException("Error: Please enter a valid email address");
        }
        userDataAccess.addUser(newUser);

        // Generate new authToken and add to database
        String authToken = generateToken();
        AuthData newAuth = new AuthData(authToken, newUser.username());
        authDataAccess.addAuth(newAuth);

        // Create register result
        return new RegisterResult(newUser.username(), authToken);
    }

    public ClearApplicationResult clearApplication(ClearApplicationRequest clearApplicationRequest) {
        gameDataAccess.clear();
        userDataAccess.clear();
        authDataAccess.clear();

        return new ClearApplicationResult();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}

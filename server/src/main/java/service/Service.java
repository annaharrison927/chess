package service;

import chess.ChessGame;
import dataaccess.*;
import request.*;
import result.*;
import model.*;

import java.util.Collections;
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

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException, BadRequestException {
        // Create new user data
        UserData newUser = new UserData(request.username(), request.password(), request.email());
        // Check if user is already in the database. If not, add the new user to database
        if (userDataAccess.getUser(newUser.username()) != null) {
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
        addToken(newUser.username(), authToken);

        // Create register result
        return new RegisterResult(newUser.username(), authToken);
    }

    public LoginResult login(LoginRequest request) throws DataAccessException, BadRequestException {
        UserData user = userDataAccess.getUser(request.username());
        // Throw exception if user has invalid login information
        if (request.username() == null) {
            throw new BadRequestException("Error: Please enter a username");
        } else if (request.password() == null) {
            throw new BadRequestException("Error: Please enter a password");
        } else if (user == null) {
            throw new DataAccessException("Error: User not in database");
        } else if (!Objects.equals(request.password(), user.password())) {
            throw new DataAccessException("Error: Incorrect password");
        }

        // Generate new authToken and add to database
        String authToken = generateToken();
        addToken(user.username(), authToken);

        // Create login result
        return new LoginResult(user.username(), authToken);
    }

    public LogoutResult logout(LogoutRequest request) throws DataAccessException {
        String authToken = request.authToken();
        checkAuth(authToken);
        authDataAccess.deleteAuth(authToken);

        return new LogoutResult();
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException, BadRequestException {
        // Check authToken
        String authToken = request.authToken();
        checkAuth(authToken);

        // Check for bad request
        if (request.gameName() == null || request.gameName().equals("null")) {
            throw new BadRequestException("Error: Please enter a game name");
        }

        int gameID = 1;
        // Create new gameID
        if (gameDataAccess.getSize() != 0) {
            int lastID = Collections.max(gameDataAccess.getIDs());
            gameID = lastID + 1;
        }

        // Add game data
        ChessGame newGame = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, request.gameName(), newGame);
        gameDataAccess.addGame(gameData);

        // Make create result
        return new CreateGameResult(gameID);
    }

    public JoinGameResult joinGame(JoinGameRequest request) throws DataAccessException, BadRequestException {
        // Check authToken
        String authToken = request.authToken();
        checkAuth(authToken);

        // Check for bad request and invalid inputs
        if (request.gameID() == 0) { // CHANGE THIS LATER!!
            throw new BadRequestException("Error: Please enter a game ID");
        } else if (gameDataAccess.getGame(request.gameID()) == null) {
            throw new DataAccessException("Error: Invalid game ID");
        }

        // Get username
        AuthData authData = authDataAccess.getAuth(authToken);
        String username = authData.username();

        // Update username
        String whiteUsername = null;
        String blackUsername = null;
        String color = request.playerColor();
        if (Objects.equals(color, "BLACK")) {
            blackUsername = username;
        } else {
            whiteUsername = username;
        }

        // Add updated data to gameDataAccess
        GameData oldGameData = gameDataAccess.getGame(request.gameID());
        String gameName = oldGameData.gameName();
        ChessGame game = oldGameData.game();
        GameData updatedGameData = new GameData(request.gameID(), whiteUsername, blackUsername, gameName, game);
        gameDataAccess.addGame(updatedGameData);

        return new JoinGameResult();
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

    private void addToken(String username, String authToken) {
        AuthData newAuth = new AuthData(authToken, username);
        authDataAccess.addAuth(newAuth);
    }

    private void checkAuth(String authToken) throws DataAccessException {
        if (authDataAccess.getAuth(authToken) == null) {
            throw new DataAccessException("Error: Invalid authToken");
        }
    }
}

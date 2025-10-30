package service;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import org.mindrot.jbcrypt.BCrypt;
import request.*;
import result.*;
import model.*;

import java.sql.SQLException;
import java.util.*;
import java.util.HashMap;

public class Service {
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;


    public Service() {
        try {
            userDataAccess = new MySQLUserDataAccess();
            authDataAccess = new MySQLAuthDataAccess();
            gameDataAccess = new MemoryGameDataAccess();
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage(), ex); // EDIT THIS LATER
        }
    }

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException, BadRequestException, DataAccessException {
        // Hash password
        String clearTextPassword = request.password();
        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());

        // Create new user data
        UserData newUser = new UserData(request.username(), hashedPassword, request.email());

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

    public JoinGameResult joinGame(JoinGameRequest request) throws DataAccessException, BadRequestException, AlreadyTakenException {
        // Check authToken
        String authToken = request.authToken();
        checkAuth(authToken);

        String color = request.playerColor();
        // Check for bad request and invalid inputs
        if (request.gameID() == 0) { // CHANGE THIS LATER!!
            throw new BadRequestException("Error: Please enter a game ID");
        } else if (gameDataAccess.getGame(request.gameID()) == null) {
            throw new DataAccessException("Error: Invalid game ID");
        } else if (!Objects.equals(color, "BLACK") && !Objects.equals(color, "WHITE")) {
            throw new BadRequestException("Error: Please enter a valid color (BLACK/WHITE)");
        }

        // Get username
        AuthData authData = authDataAccess.getAuth(authToken);
        String username = authData.username();

        GameData oldGameData = gameDataAccess.getGame(request.gameID());
        // Update username
        String whiteUsername = oldGameData.whiteUsername();
        String blackUsername = oldGameData.blackUsername();

        if (Objects.equals(color, "BLACK")) {
            if (blackUsername != null) {
                throw new AlreadyTakenException("Error: Color already taken");
            }
            blackUsername = username;
        } else {
            if (whiteUsername != null) {
                throw new AlreadyTakenException("Error: Color already taken");
            }
            whiteUsername = username;
        }

        // Add updated data to gameDataAccess
        String gameName = oldGameData.gameName();
        ChessGame game = oldGameData.game();
        GameData updatedGameData = new GameData(request.gameID(), whiteUsername, blackUsername, gameName, game);
        gameDataAccess.addGame(updatedGameData);

        return new JoinGameResult();
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
        // Check authToken
        String authToken = request.authToken();
        checkAuth(authToken);

        Collection<GameData> games = gameDataAccess.listGames().values();

        return new ListGamesResult(games);
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

    private void addToken(String username, String authToken) throws DataAccessException {
        AuthData newAuth = new AuthData(authToken, username);
        authDataAccess.addAuth(newAuth);
    }

    private void checkAuth(String authToken) throws DataAccessException {
        if (authDataAccess.getAuth(authToken) == null) {
            throw new DataAccessException("Error: Invalid authToken");
        }
    }

}

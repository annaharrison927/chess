package service;

import dataaccess.DataAccessException;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.*;
import result.CreateGameResult;
import result.LoginResult;
import service.Service;

public class ServiceTest {

    private static Service service;
    private String existingAuth;

    @BeforeAll
    public static void init() {
        service = new Service();
    }

    @BeforeEach
    public void setup() {
        service.clearApplication();
    }

    @Test
    void registerGood() {

        RegisterRequest registerRequest = new RegisterRequest("Banana Man", "iLiKeC00kies123", "monkeyzarecool@gmail.com");
        Assertions.assertDoesNotThrow(() -> {
            service.register(registerRequest);
        });
    }

    @Test
    void registerBad() {

        RegisterRequest registerRequest = new RegisterRequest("Batman", "supermanStinks", null);
        Assertions.assertThrows(BadRequestException.class, () -> service.register(registerRequest));
    }

    @Test
    void loginGood() throws DataAccessException {

        RegisterRequest registerRequest = new RegisterRequest("Grandma", "whatsmypasswordagain??", "gertrudejones1947@hotmail.com");
        service.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("Grandma", "whatsmypasswordagain??");
        Assertions.assertDoesNotThrow(() -> {
            service.login(loginRequest);
        });
    }

    @Test
    void loginBad() throws DataAccessException {

        RegisterRequest registerRequest = new RegisterRequest("Grandma", "whatsmypasswordagain??", "gertrudejones1947@hotmail.com");
        service.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("Grandma", "iforgotmypassword");
        Assertions.assertThrows(DataAccessException.class, () -> service.login(loginRequest));
    }

    @Test
    void logoutGood() throws DataAccessException {

        RegisterRequest registerRequest = new RegisterRequest("Grandma", "whatsmypasswordagain??", "gertrudejones1947@hotmail.com");
        service.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("Grandma", "whatsmypasswordagain??");
        LoginResult loginResult = service.login(loginRequest);
        String authToken = loginResult.authToken();

        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        Assertions.assertDoesNotThrow(() -> {
            service.logout(logoutRequest);
        });
    }

    @Test
    void logoutBad() {

        String authToken = null;

        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        Assertions.assertThrows(DataAccessException.class, () -> service.logout(logoutRequest));
    }

    @Test
    void createGameGood() throws DataAccessException {

        RegisterRequest registerRequest = new RegisterRequest("Grandma", "whatsmypasswordagain??", "gertrudejones1947@hotmail.com");
        service.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("Grandma", "whatsmypasswordagain??");
        LoginResult loginResult = service.login(loginRequest);
        String authToken = loginResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("yay chess time!", authToken);
        Assertions.assertDoesNotThrow(() -> {
            service.createGame(createGameRequest);
        });
    }

    @Test
    void createGameBad() throws DataAccessException {

        RegisterRequest registerRequest = new RegisterRequest("Grandma", "whatsmypasswordagain??", "gertrudejones1947@hotmail.com");
        service.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("Grandma", "whatsmypasswordagain??");
        LoginResult loginResult = service.login(loginRequest);
        String authToken = loginResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest(null, authToken);
        Assertions.assertThrows(BadRequestException.class, () -> service.createGame(createGameRequest));
    }

    @Test
    void joinGameGood() throws DataAccessException {

        RegisterRequest registerRequest = new RegisterRequest("Grandma", "whatsmypasswordagain??", "gertrudejones1947@hotmail.com");
        service.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("Grandma", "whatsmypasswordagain??");
        LoginResult loginResult = service.login(loginRequest);
        String authToken = loginResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("yay chess time!", authToken);
        CreateGameResult createGameResult = service.createGame(createGameRequest);
        int gameID = createGameResult.gameID();

        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameID, authToken);
        Assertions.assertDoesNotThrow(() -> {
            service.joinGame(joinGameRequest);
        });
    }

    @Test
    void joinGameBad() throws DataAccessException {

        RegisterRequest registerRequest = new RegisterRequest("Grandma", "whatsmypasswordagain??", "gertrudejones1947@hotmail.com");
        service.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("Grandma", "whatsmypasswordagain??");
        LoginResult loginResult = service.login(loginRequest);
        String authToken = loginResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("yay chess time!", authToken);
        CreateGameResult createGameResult = service.createGame(createGameRequest);
        int gameID = createGameResult.gameID();

        JoinGameRequest joinGameRequest = new JoinGameRequest("RAINBOW", gameID, authToken);
        Assertions.assertThrows(BadRequestException.class, () -> service.joinGame(joinGameRequest));
    }

    @Test
    void listGamesGood() throws DataAccessException {

        RegisterRequest registerRequest = new RegisterRequest("Grandma", "whatsmypasswordagain??", "gertrudejones1947@hotmail.com");
        service.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("Grandma", "whatsmypasswordagain??");
        LoginResult loginResult = service.login(loginRequest);
        String authToken = loginResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("yay chess time!", authToken);
        service.createGame(createGameRequest);

        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        Assertions.assertDoesNotThrow(() -> {
            service.listGames(listGamesRequest);
        });
    }

    @Test
    void listGamesBad() {

        ListGamesRequest listGamesRequest = new ListGamesRequest(null);
        Assertions.assertThrows(DataAccessException.class, () -> service.listGames(listGamesRequest));
    }

    @Test
    void clearApplication() throws DataAccessException {

        RegisterRequest registerRequest = new RegisterRequest("Grandma", "whatsmypasswordagain??", "gertrudejones1947@hotmail.com");
        service.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("Grandma", "whatsmypasswordagain??");
        LoginResult loginResult = service.login(loginRequest);
        String authToken = loginResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("yay chess time!", authToken);
        service.createGame(createGameRequest);

        ClearApplicationRequest clearApplicationRequest = new ClearApplicationRequest();
        Assertions.assertDoesNotThrow(() -> {
            service.clearApplication(clearApplicationRequest);
        });
    }
}

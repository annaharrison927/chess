package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import model.GameData;
import model.UserData;
import request.*;
import result.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private String authToken;
    private Map<Integer, Integer> idLibrary = new HashMap<>();

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void register(UserData userData) throws Exception {
        var request = buildRequest("POST", "/user", userData, null);
        var response = sendRequest(request);
        RegisterResult registerResult = new Gson().fromJson(response.body(), RegisterResult.class);
        authToken = registerResult.authToken();
        handleResponse(response, RegisterResult.class);
    }

    public void login(LoginRequest loginRequest) throws Exception {
        var request = buildRequest("POST", "/session", loginRequest, null);
        var response = sendRequest(request);
        LoginResult loginResult = new Gson().fromJson(response.body(), LoginResult.class);
        authToken = loginResult.authToken();
        handleResponse(response, LoginResult.class);
    }

    public void logout() throws Exception {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        var request = buildRequest("DELETE", "/session", logoutRequest, logoutRequest.authToken());
        var response = sendRequest(request);
        authToken = null;
        handleResponse(response, LogoutResult.class);
    }

    public void create(String gameName) throws Exception {
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName, authToken);
        var request = buildRequest("POST", "/game", createGameRequest, authToken);
        var response = sendRequest(request);
        handleResponse(response, CreateGameRequest.class);
    }

    public Collection<String> list() throws Exception {
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        var request = buildRequest("GET", "/game", listGamesRequest, authToken);
        var response = sendRequest(request);
        handleResponse(response, ListGamesResult.class);

        ListGamesResult listGamesResult = new Gson().fromJson(response.body(), ListGamesResult.class);
        Collection<GameData> games = listGamesResult.games();
        idLibrary.clear();
        Collection<String> gameList = new ArrayList<>();
        int i = 1;
        for (GameData gameData : games) {
            int gameID = gameData.gameID();
            idLibrary.put(i, gameID);

            String gameStr = getGameStr(gameData, i);
            gameList.add(gameStr);
            i++;
        }
        return gameList;
    }

    private static String getGameStr(GameData gameData, int i) {
        String whitePlayer = "[Available: No player yet]";
        String blackPlayer = "[Available: No player yet]";
        if (gameData.whiteUsername() != null) {
            whitePlayer = gameData.whiteUsername();
        }
        if (gameData.blackUsername() != null) {
            blackPlayer = gameData.blackUsername();
        }

        String gameName = gameData.gameName();
        return String.format("%d. %s (WHITE team: %s; BLACK team: %s)\n", i, gameName, whitePlayer, blackPlayer);
    }


    public void join(int id, String color) throws Exception {
        int gameID = idLibrary.get(id);
        JoinGameRequest joinGameRequest = new JoinGameRequest(color, gameID, authToken);
        var request = buildRequest("PUT", "/game", joinGameRequest, authToken);
        var response = sendRequest(request);
        handleResponse(response, JoinGameResult.class);
    }

    public void clear() throws Exception {
        ClearApplicationRequest clearApplicationRequest = new ClearApplicationRequest();
        var request = buildRequest("DELETE", "/db", clearApplicationRequest, null);
        var response = sendRequest(request);
        handleResponse(response, ClearApplicationResult.class);
    }


    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("authorization", authToken);
        }
        return request.build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new Exception("Error: " + ex.getMessage() + "\n");
        }
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private <T> void handleResponse(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        var status = response.statusCode();
        if (status != 200) {
            var body = response.body();
            if (body != null) {
                if (status == 403) {
                    throw new AlreadyTakenException("Error: Already taken" + "\n");
                } else if (status == 401) {
                    throw new DataAccessException("Error: Unauthorized" + "\n");
                } else if (status == 400) {
                    throw new BadRequestException("Error: Invalid input" + "\n");
                } else if (status == 500) {
                    throw new DataAccessException("Error: Failed to connect" + "\n");
                }
            }
            throw new Exception("Error: Other issue" + "\n");
        }

        if (responseClass != null) {
            new Gson().fromJson(response.body(), responseClass);
        }

    }

}

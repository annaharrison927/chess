package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import request.*;
import result.*;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.Service;

import java.util.Map;
import java.util.Objects;

public class Server {

    private final Javalin server;
    private final Service service = new Service();

    public Server() {
        server = Javalin.create(config -> config.staticFiles.add("web"));

        // Endpoints:
        server.delete("db", this::clearApplication);
        server.post("user", this::register);
        server.post("session", this::login);
        server.delete("session", this::logout);
        server.post("game", this::createGame);
        server.put("game", this::joinGame);
        server.get("game", this::listGames);
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }

    private void register(Context ctx) {
        var serializer = new Gson();
        String reqJson = ctx.body();
        RegisterRequest req = serializer.fromJson(reqJson, RegisterRequest.class);
        // Call to the service and register
        try {
            RegisterResult res = service.register(req);
            ctx.result(serializer.toJson(res));
        } catch (AlreadyTakenException ex) {
            ctx.status(403);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        } catch (BadRequestException ex) {
            ctx.status(400);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        } catch (DataAccessException ex) {
            ctx.status(500);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
            ;
        }
    }

    private void login(Context ctx) {
        var serializer = new Gson();
        String reqJson = ctx.body();
        LoginRequest req = serializer.fromJson(reqJson, LoginRequest.class);

        try {
            LoginResult res = service.login(req);
            ctx.result(serializer.toJson(res));
        } catch (BadRequestException ex) {
            ctx.status(400);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        } catch (DataAccessException ex) {
            chooseStatusCode(ctx, ex);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        }
    }

    private void logout(Context ctx) {
        var serializer = new Gson();
        String authToken = ctx.header("authorization");
        String reqJson = String.format("{ \"authToken\":\"%s\" }", authToken);
        LogoutRequest req = serializer.fromJson(reqJson, LogoutRequest.class);
        try {
            LogoutResult res = service.logout(req);
            ctx.result(serializer.toJson(res));
        } catch (DataAccessException ex) {
            chooseStatusCode(ctx, ex);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        }

    }

    private void createGame(Context ctx) {
        // Extract needed info from header and body
        var serializer = new Gson();
        String authToken = ctx.header("authorization");
        String jsonBody = ctx.body();
        String gameName = null;
        if (!jsonBody.equals("{}")) {
            JsonElement jsonParsed = JsonParser.parseString(jsonBody);
            JsonObject jsonObject = jsonParsed.getAsJsonObject();
            gameName = jsonObject.get("gameName").getAsString();
        }

        // Make request
        String reqJson = String.format("{ \"gameName\":\"%s\", \"authToken\":\"%s\" }", gameName, authToken);
        CreateGameRequest req = serializer.fromJson(reqJson, CreateGameRequest.class);

        // Get result from service
        try {
            CreateGameResult res = service.createGame(req);
            ctx.result(serializer.toJson(res));
        } catch (DataAccessException ex) {
            chooseStatusCode(ctx, ex);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        } catch (BadRequestException ex) {
            ctx.status(400);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        }
    }

    private void joinGame(Context ctx) {
        var serializer = new Gson();
        String authToken = ctx.header("authorization");
        String jsonBody = ctx.body();
        int gameID = 0;
        String playerColor = "";
        if (!jsonBody.equals("{}")) {
            JsonElement jsonParsed = JsonParser.parseString(jsonBody);
            JsonObject jsonObject = jsonParsed.getAsJsonObject();
            if (jsonObject.get("gameID") != null) {
                gameID = jsonObject.get("gameID").getAsInt();
            }
            if (jsonObject.get("playerColor") != null) {
                playerColor = jsonObject.get("playerColor").getAsString();
            }
        }

        // Make request
        String reqJson = String.format("{ \"playerColor\":\"%s\", \"gameID\":\"%d\", \"authToken\":\"%s\" }", playerColor, gameID, authToken);
        JoinGameRequest req = serializer.fromJson(reqJson, JoinGameRequest.class);

        // Get result from service
        try {
            JoinGameResult res = service.joinGame(req);
            ctx.result(serializer.toJson(res));
        } catch (DataAccessException ex) {
            chooseStatusCode(ctx, ex);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        } catch (BadRequestException ex) {
            ctx.status(400);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        } catch (AlreadyTakenException ex) {
            ctx.status(403);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        }
    }

    private void listGames(Context ctx) throws DataAccessException {
        var serializer = new Gson();
        String authToken = ctx.header("authorization");
        ListGamesRequest req = new ListGamesRequest(authToken);
        try {
            ListGamesResult res = service.listGames(req);
            ctx.result(serializer.toJson(res));
        } catch (DataAccessException ex) {
            chooseStatusCode(ctx, ex);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        }

    }

    private void clearApplication(Context ctx) throws DataAccessException {
        var serializer = new Gson();
        String reqJson = ctx.body();
        ClearApplicationRequest req = serializer.fromJson(reqJson, ClearApplicationRequest.class);
        try {
            ClearApplicationResult res = service.clearApplication(req);
            ctx.result(serializer.toJson(res));
        } catch (DataAccessException ex) {
            ctx.status(500);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        }
    }

    private void chooseStatusCode(Context ctx, DataAccessException ex) {
        if (Objects.equals(ex.getMessage(), "Error: failed to get connection")) {
            ctx.status(500);
        } else {
            ctx.status(401);
        }
    }


}

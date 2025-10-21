package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import request.*;
import result.*;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.Service;

import java.util.Map;

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
            ctx.status(401);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        }
    }

    private void logout(Context ctx) {
        var serializer = new Gson();
        String authToken = ctx.header("authorization");
        String reqJson = String.format("{ \"authToken\":\"%s\" }", authToken);
        LogoutRequest req = serializer.fromJson(reqJson, LogoutRequest.class);
        String practice = """
                        {"username" : "bob"}
                """;
        try {
            LogoutResult res = service.logout(req);
            ctx.result(serializer.toJson(res));
        } catch (DataAccessException ex) {
            ctx.status(401);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        }

    }

    private void clearApplication(Context ctx) {
        var serializer = new Gson();
        String reqJson = ctx.body();
        ClearApplicationRequest req = serializer.fromJson(reqJson, ClearApplicationRequest.class);
        ClearApplicationResult res = service.clearApplication(req);
        ctx.result(serializer.toJson(res));
    }


}

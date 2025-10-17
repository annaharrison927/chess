package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import record.RegisterResult;
import request.RegisterRequest;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.Service;

import java.util.Map;

public class Server {

    private final Javalin server;
    private final Service userService = new Service();

    public Server() {
        server = Javalin.create(config -> config.staticFiles.add("web"));

        // These are just hard coded values!!
        server.delete("db", this::delete);
        server.post("user", this::register);

        // Register your endpoints and exception handlers here.

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
            RegisterResult res = userService.register(req);
            ctx.result(serializer.toJson(res));
        } catch (AlreadyTakenException ex) {
            ctx.status(403);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        } catch (BadRequestException ex) {
            ctx.status(400);
            ctx.result(serializer.toJson(Map.of("message", ex.getMessage())));
        }
    }

    private void delete(Context ctx) {
    }


}

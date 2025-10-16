package server;

import com.google.gson.Gson;
import dataaccess.UserDataAccess;
import dataaccess.MemoryUserDataAccess;
import model.UserData;
import io.javalin.*;
import io.javalin.http.Context;
import record.RegisterResult;
import request.RegisterRequest;
import service.UserService;

public class Server {

    private final Javalin server;
    private UserService userService = new UserService();
    private UserDataAccess dataAccess;

    public Server() {
        dataAccess = new MemoryUserDataAccess();
        server = Javalin.create(config -> config.staticFiles.add("web"));

        // These are just hard coded values!!
        server.delete("db", ctx -> ctx.result("{}"));
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
//        req.put("authToken", "cow");
        RegisterResult res = userService.register(req);

//      serializer.fromJson(ctx.body(), Map.class);
        ctx.result(serializer.toJson(res));
    }


}

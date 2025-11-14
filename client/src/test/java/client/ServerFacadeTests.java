package client;

import model.UserData;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import server.Server;
import service.BadRequestException;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    private final UserData goodUserData = new UserData("Batman", "SupermanStinks", "nananana@gmail.com");
    private final UserData badUserData = new UserData("Grandma", null, "booya@hotmail.com");

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = String.format("http://localhost:%d", port);
        serverFacade = new ServerFacade(url);
    }
    
    @BeforeEach
    public void setup() throws Exception {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    void registerGood() {
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.register(goodUserData);
        });
    }

    @Test
    void registerBad() {
        Assertions.assertThrows(Exception.class, () -> serverFacade.register(badUserData));
    }

    @Test
    void loginGood() throws Exception {
        serverFacade.register(goodUserData);
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.login(new LoginRequest(goodUserData.username(), goodUserData.password()));
        });
    }

    @Test
    void loginBad() {
        Assertions.assertThrows(Exception.class, () -> serverFacade.login(new LoginRequest("Grandma", null)));
    }

    @Test
    void logoutGood() throws Exception {
        serverFacade.register(goodUserData);
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.logout();
        });
    }

    @Test
    void logoutBad() {
        Assertions.assertThrows(Exception.class, () -> serverFacade.logout());
    }

    @Test
    void createGood() throws Exception {
        serverFacade.register(goodUserData);
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.create("good_game");
        });
    }

    @Test
    void createBad() throws Exception {
        serverFacade.register(goodUserData);
        Assertions.assertThrows(Exception.class, () -> serverFacade.create(null));
    }

    @Test
    void listGood() throws Exception {
        serverFacade.register(goodUserData);
        serverFacade.create("good_game");
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.list();
        });
    }

    @Test
    void listBad() {
        Assertions.assertThrows(Exception.class, () -> serverFacade.list());
    }

    @Test
    void joinGood() throws Exception {
        serverFacade.register(goodUserData);
        serverFacade.create("good_game");
        serverFacade.list();
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.join(1, "WHITE");
        });

    }

    @Test
    void joinBad() throws Exception {
        serverFacade.register(goodUserData);
        serverFacade.create("good_game");
        serverFacade.list();
        Assertions.assertThrows(Exception.class, () -> serverFacade.join(2, "WHITE"));
    }

}

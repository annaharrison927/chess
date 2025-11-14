package ui;

import com.mysql.cj.log.Log;
import model.UserData;
import request.CreateGameRequest;
import request.LoginRequest;

import javax.swing.plaf.nimbus.State;

import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade serverFacade;
    private Collection<String> gameList;
    private State state = State.LOGGED_OUT;

    public Client(String serverUrl) throws Exception {
        serverFacade = new ServerFacade(serverUrl);
    }

    public enum State {
        LOGGED_IN,
        LOGGED_OUT,
        IN_GAME
    }


    public void run() {
        System.out.println("Welcome to Chess! Select an option from the menu.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();

            try {
                result = evaluate(line);
                System.out.print(result);
            } catch (Throwable e) {
                String message = e.toString();
                System.out.print(message);
            }
        }
        System.out.println();
    }

    public String evaluate(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String command = "help";
            if (tokens.length > 0) {
                command = tokens[0];
            }
            String[] parameters = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (command) {
                case "register" -> register(parameters);
                case "login" -> login(parameters);
                case "logout" -> logout();
                case "create" -> create(parameters);
                case "list" -> list();
                case "join" -> join(parameters);
                case "observe" -> observe(parameters);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception {
        if (params.length != 3) {
            throw new Exception("Error: Please enter a valid username, password, and email" + "\n");
        }

        String username = params[0];
        String password = params[1];
        String email = params[2];
        UserData userData = new UserData(username, password, email);

        serverFacade.register(userData);
        serverFacade.login(new LoginRequest(userData.username(), userData.password()));
        state = State.LOGGED_IN;

        return String.format("Hi %s, you've successfully registered! \n", username);
    }

    public String login(String... params) throws Exception {
        if (state == State.LOGGED_IN) {
            throw new Exception("Error: You are already logged in! \n");
        }
        if (params.length != 2) {
            throw new Exception("Error: Please enter your username and password \n");
        }

        String username = params[0];
        String password = params[1];
        LoginRequest loginRequest = new LoginRequest(username, password);

        serverFacade.login(loginRequest);
        state = State.LOGGED_IN;

        return String.format("%s, you are logged in! \n", username);
    }

    public String logout() throws Exception {
        assertLoggedIn();
        serverFacade.logout();
        state = State.LOGGED_OUT;

        return "You have successfully logged out! \n";
    }

    public String create(String... params) throws Exception {
        assertLoggedIn();
        if (params.length != 1) {
            throw new Exception("Error: Please enter a game name \n");
        }

        String gameName = params[0];
        serverFacade.create(gameName);

        return String.format("You created a new game: %s \n", gameName);
    }

    public String list() throws Exception {
        assertLoggedIn();
        gameList = serverFacade.list();
        return String.join("", gameList);
    }

    public String join(String... params) throws Exception {
        assertLoggedIn();
        if (params.length != 2) {
            throw new Exception("Error: Please enter a game id and your team color (WHITE/BLACK) \n");
        }

        int id = checkID(params);

        String color = params[1].toUpperCase();

        serverFacade.join(id, color);
        state = State.IN_GAME;
        Board startBoard = new Board();
        startBoard.createBoard(color);
        return String.format("You joined game #%d as the %s player!\n", id, color);
    }

    public String observe(String... params) throws Exception {
        assertLoggedIn();
        if (params.length != 1) {
            throw new Exception("Error: Please enter the id of the game you would like to observe \n");
        }

        int id = checkID(params);

        Board startBoard = new Board();
        startBoard.createBoard("WHITE");
        return String.format("You are now observing game #%d!\n", id);
    }

    public String help() {
        if (state == State.LOGGED_OUT) {
            return """
                    * register (username, password, email)
                    * login (username, password)
                    * quit
                    * help
                    """;
        } else {
            return """
                    * create (name) - create a new game
                    * list - list all games
                    * join (id, WHITE/BLACK) - join a game
                    * observe (id) - observe a game
                    * logout
                    * quit
                    * help
                    """;
        }
    }

    private void assertLoggedIn() throws Exception {
        if (state == State.LOGGED_OUT) {
            throw new Exception("Error: You're not logged in! \n");
        }
    }

    private int checkID(String... params) throws Exception {
        int id;

        try {
            id = Integer.parseInt(params[0]);
        } catch (Exception ex) {
            throw new Exception("Error: Please enter the number of the game you would like to play, " +
                    "followed by team color\n");
        }

        if (gameList == null) {
            throw new Exception("Error: Please list games before joining a game");
        }

        if (id > gameList.size() || id < 1) {
            throw new Exception("Error: Invalid game ID\n");
        }

        return id;
    }

}

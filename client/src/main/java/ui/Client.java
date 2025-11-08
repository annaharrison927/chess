package ui;

import com.mysql.cj.log.Log;
import model.UserData;
import request.LoginRequest;

import javax.swing.plaf.nimbus.State;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade serverFacade;
    private boolean loggedIn = false;

    public Client(String serverUrl) throws Exception {
        serverFacade = new ServerFacade(serverUrl);
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
        loggedIn = true;

        return String.format("Hi %s, you've successfully registered! \n", username);
    }

    public String login(String... params) throws Exception {
        if (loggedIn) {
            throw new Exception("Error: You are already logged in! \n");
        }
        if (params.length != 2) {
            throw new Exception("Error: Please enter your username and password \n");
        }

        String username = params[0];
        String password = params[1];
        LoginRequest loginRequest = new LoginRequest(username, password);

        serverFacade.login(loginRequest);
        loggedIn = true;

        return String.format("%s, you are logged in! \n", username);
    }

//    public String logout() throws Exception {
//
//    }

    public String help() {
        if (!loggedIn) {
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

}

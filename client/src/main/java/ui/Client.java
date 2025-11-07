package ui;

import javax.swing.plaf.nimbus.State;

import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade serverFacade;
    private boolean signedIn = false;

    public Client(String serverUrl) throws Exception {
        serverFacade = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to Chess! Select an option from the menu.");
        System.out.print("help");
    }

}

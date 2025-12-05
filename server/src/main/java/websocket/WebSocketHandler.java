package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import server.Server;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.swing.*;
import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;

    public WebSocketHandler() {
        try {
            DatabaseManager.createDatabase();
            userDataAccess = new MySQLUserDataAccess();
            authDataAccess = new MySQLAuthDataAccess();
            gameDataAccess = new MySQLGameDataAccess();
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        System.out.println("Websocket Closed");
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) throws Exception {
        System.out.println("Websocket Connected");
        wsConnectContext.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        UserGameCommand userGameCommand = new Gson().fromJson(wsMessageContext.message(), UserGameCommand.class);
        UserGameCommand.CommandType commandType = userGameCommand.getCommandType();
        String authToken = userGameCommand.getAuthToken();
        Integer gameID = userGameCommand.getGameID();
        switch (commandType) {
            case CONNECT -> connect(wsMessageContext.session, authToken, gameID);
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }

    private void connect(Session session, String authToken, Integer gameID) throws Exception {
        connections.add(session);
        String gameName = gameDataAccess.getGame(gameID).gameName();
        String username = authDataAccess.getAuth(authToken).username();

        ChessGame.TeamColor color = null;
        if (username.equals(gameDataAccess.getGame(gameID).blackUsername())) {
            color = ChessGame.TeamColor.BLACK;
        } else if (username.equals(gameDataAccess.getGame(gameID).whiteUsername())) {
            color = ChessGame.TeamColor.WHITE;
        }

        String role;
        switch (color) {
            case null -> role = "an observer";
            case BLACK -> role = "the black player";
            case WHITE -> role = "the white player";
        }

        LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                String.format("Your game (%s) has loaded", gameName));
        connections.broadcast(session, "root", loadGameMessage);

        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s has joined the game as %s", username, role));
        connections.broadcast(session, "others", notificationMessage);
    }

    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }


}

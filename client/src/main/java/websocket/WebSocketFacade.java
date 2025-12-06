package websocket;

import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.URI;

public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageHandler serverMessageHandler;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public WebSocketFacade(String url, ServerMessageHandler serverMessageHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                ServerMessage.ServerMessageType serverMessageType = serverMessage.getServerMessageType();
                switch (serverMessageType) {
                    case LOAD_GAME -> {
                        LoadGameMessage lMessage = new Gson().fromJson(message, LoadGameMessage.class);
                        serverMessageHandler.notify(lMessage);
                    }
                    case NOTIFICATION -> {
                        NotificationMessage nMessage = new Gson().fromJson(message, NotificationMessage.class);
                        serverMessageHandler.notify(nMessage);
                    }
                    case ERROR -> {
                        ErrorMessage eMessage = new Gson().fromJson(message, ErrorMessage.class);
                        serverMessageHandler.notify(eMessage);
                    }
                }
            });
        } catch (Exception ex) {
            throw new Exception("Error: " + ex.getMessage());
        }
    }

    public void connect(String authToken, int gameID) throws Exception {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new Exception("Error: " + ex.getMessage());
        }
    }

    public void makeMove() {

    }

    public void leave() {

    }

    public void resign() {

    }
}

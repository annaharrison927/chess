package websocket;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import server.Server;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.swing.*;
import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();

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
        switch (commandType) {
            case CONNECT -> connect(wsMessageContext.session);
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }

    private void connect(Session session) throws IOException {
        connections.add(session);
        ServerMessage load_game = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(session, "root", load_game);
    }

    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }


}

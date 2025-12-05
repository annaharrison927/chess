package websocket.messages;

public class ErrorMessage extends ServerMessage {
    ServerMessageType serverMessageType;
    String errorMessage;

    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.serverMessageType = type;
        this.errorMessage = errorMessage;

    }
}

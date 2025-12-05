package websocket.messages;

public class NotificationMessage extends ServerMessage {
    ServerMessageType serverMessageType;
    String message;

    public NotificationMessage(ServerMessageType type, String message) {
        super(type);
        this.serverMessageType = type;
        this.message = message;
    }
}

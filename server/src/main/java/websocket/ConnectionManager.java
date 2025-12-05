package websocket;


import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, Session> connections = new ConcurrentHashMap<>();

    public void add(Session session) {
        connections.put(session, session);
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(Session rootSession, String broadcastType, ServerMessage serverMessage) throws IOException {
        String message = serverMessage.toString();
        for (Session session : connections.values()) {
            if (session.isOpen()) {
                switch (broadcastType) {
                    case "all" -> broadcastToAll(session, message);
                    case "root" -> broadcastToRoot(rootSession, session, message);
                    case "others" -> broadcastExcludeRoot(rootSession, session, message);
                }
            }
        }
    }

    private void broadcastToAll(Session session, String message) throws IOException {
        session.getRemote().sendString(message);
    }

    private void broadcastToRoot(Session rootSession, Session session, String message) throws IOException {
        if (session.equals(rootSession)) {
            session.getRemote().sendString(message);
        }
    }

    private void broadcastExcludeRoot(Session rootSession, Session session, String message) throws IOException {
        if (!session.equals(rootSession)) {
            session.getRemote().sendString(message);
        }
    }
}

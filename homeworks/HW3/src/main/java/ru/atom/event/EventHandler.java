package ru.atom.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import ru.atom.network.Broker;

public class EventHandler extends WebSocketAdapter {
    private static final Logger log = LogManager.getLogger(EventHandler.class);

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        log.info("Socket Connected: " + session);
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        log.info("Received TEXT message: " + message);
        Broker.getInstance().receive(super.getSession(), message);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        Session oldSession = super.getSession();
        super.onWebSocketClose(statusCode, reason);
        log.info("Socket Closed: [" + statusCode + "] " + reason);
        Broker.getInstance().removeSession(oldSession);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        log.error("", cause);
    }
}

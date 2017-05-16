package ru.atom.game.server.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import ru.atom.game.server.communication.AuthClient;
import ru.atom.game.server.controller.GameController;
import ru.atom.game.server.message.Message;
import ru.atom.game.server.message.Topic;
import ru.atom.game.server.util.JsonHelper;

import java.awt.datatransfer.Clipboard;
import java.io.IOException;


public class Broker {
    private static final Logger log = LogManager.getLogger(Broker.class);

    private static final Broker instance = new Broker();
    private final ConnectionPool connectionPool;
    private final GameController gameController;

    public static Broker getInstance() {
        return instance;
    }

    private Broker() {
        this.connectionPool = ConnectionPool.getInstance();
        this.gameController = GameController.getInstance();
    }

    public void removeSession(@NotNull Session session) {
        connectionPool.remove(session);
    }

    public void receive(@NotNull Session session, @NotNull String msg) {
        log.info("RECEIVED: {}", msg);
        Message message = JsonHelper.fromJson(msg, Message.class);
        Topic topic = message.getTopic();

        if (topic == Topic.HELLO) {
            onMsgHello(message, session);
            return;
        }

        String player = connectionPool.getPlayer(session);
        if (player != null) {
            gameController.onMsgHandler(player, message);
        }
    }

    public void send(@NotNull String player, @NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, object));
        Session session = connectionPool.getSession(player);
        if (session != null) {
            log.debug("msg to {}: {}", player, message);
            connectionPool.send(session, message);
        }
    }

    public void send(@NotNull Session session, @NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, object));
        log.debug("msg to session: {}", message);
        connectionPool.send(session, message);
    }


    public void broadcast(@NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, object));
        log.debug("broadcast: {}", message);
        connectionPool.broadcast(message);
    }

    private void onMsgHello(Message message, Session session) {
        String player = message.getData();
        boolean isInvalidToken = true;
        String errorMsg = "";
        try {
            okhttp3.Response response = AuthClient.isLogined("-9190499099843723714");
            if (response.code() == 200) {
                isInvalidToken = false;
            } else {
                errorMsg = response.toString();
            }
        } catch (IOException e) {
            errorMsg = e.getMessage();
        }

        if (isInvalidToken) {
            send(session, Topic.INVALID_TOKEN, errorMsg);
            return;
        }

        connectionPool.add(session, player);
        gameController.addPlayerAndStartGame(player);
    }
}

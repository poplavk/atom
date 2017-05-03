package ru.atom.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import ru.atom.controller.GameController;
import ru.atom.message.Message;
import ru.atom.message.Topic;
import ru.atom.model.GameSession;
import ru.atom.model.Girl;
import ru.atom.util.JsonHelper;


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

    public void receive(@NotNull Session session, @NotNull String msg) {
        log.info("RECEIVED by {}: {}", session.toString(), msg);
        Message message = JsonHelper.fromJson(msg, Message.class);
        Topic topic = message.getTopic();

        if (topic==Topic.HELLO) {
            String player = message.getData();
            connectionPool.add(session, player);
            gameController.addPlayerAndStartGame(player);
            return;
        }

        String player = connectionPool.getPlayer(session);
        gameController.onMsgHandler(player, message);
    }

    public void send(@NotNull String player, @NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        Session session = connectionPool.getSession(player);
        log.info("msg to {}: {}",player, message);
        connectionPool.send(session, message);
    }

    public void broadcast(@NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        log.info("broadcast: {}", message);
        connectionPool.broadcast(message);
    }

}

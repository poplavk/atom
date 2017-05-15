package ru.atom.game.server.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import ru.atom.game.server.controller.GameController;
import ru.atom.game.server.message.Message;
import ru.atom.game.server.message.Topic;
import ru.atom.game.server.util.JsonHelper;


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
            String player = message.getData();
            connectionPool.add(session, player);
            gameController.addPlayerAndStartGame(player);

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

    public void broadcast(@NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, object));
        log.debug("broadcast: {}", message);
        connectionPool.broadcast(message);
    }

}

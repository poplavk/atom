package ru.atom.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import ru.atom.message.Message;
import ru.atom.message.Topic;
import ru.atom.model.GameSession;
import ru.atom.model.Girl;
import ru.atom.util.JsonHelper;


public class Broker {
    private static final Logger log = LogManager.getLogger(Broker.class);

    private static final Broker instance = new Broker();
    private final ConnectionPool connectionPool;

    public static Broker getInstance() {
        return instance;
    }

    private Broker() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public void receive(@NotNull Session session, @NotNull String msg) {
        log.info("RECEIVED: " + msg);
        Message message = JsonHelper.fromJson(msg, Message.class);
        //TODO TASK2 implement message processing

        // TODO: 4/30/17 вынести обработку в отдельный класс?
        Topic topic = message.getTopic();
        if (topic==Topic.HELLO) {
            connectionPool.add(session, message.getData());
            // TODO: 4/30/17 создать девочку
            return;
        }
        if (topic == Topic.MOVE) {
            // TODO: 4/30/17 вытащить направление, по тику передвинуть девочку
            return;
        }
        if (topic==Topic.PLANT_BOMB) {
            // TODO: 4/30/17 на текущей координате девочки разместить бомбу
            return;
        }

    }

    public void send(@NotNull String player, @NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        Session session = connectionPool.getSession(player);
        connectionPool.send(session, message);
    }

    public void broadcast(@NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        connectionPool.broadcast(message);
    }

}

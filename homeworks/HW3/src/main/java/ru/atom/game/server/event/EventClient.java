package ru.atom.game.server.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import ru.atom.game.server.message.DirectionMsg;
import ru.atom.game.server.message.Message;
import ru.atom.game.server.message.Topic;
import ru.atom.game.server.model.Movable;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Future;

public class EventClient {
    private static final Logger log = LogManager.getLogger(EventClient.class);

    private final URI uri = URI.create("ws://localhost:8089/events/");

    private Session session;

    public void connect() {
        WebSocketClient client = new WebSocketClient();

        try {
            client.start();
        } catch (Exception e) {
            log.error("Не удалось выполнить client.start()", e);
            return;
        }
        // The socket that receives events
        EventHandler socket = new EventHandler();
        try {
            // Attempt Connect
            Future<Session> fut = client.connect(socket, uri);
            // Wait for Connect
            session = fut.get();

        } catch (Exception e) {
            log.error("Не удалось подключиться", e);
        }

    }

    public void sendHello(String name) {
        Message message = new Message(Topic.HELLO, name);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(message);
            log.info(json);
            session.getRemote().sendString(json);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при преобразовании объекта Message в json", e);
        } catch (IOException e) {
            log.error("Ошибка при отправке сообщения {}", json);
        }
    }
    public void sendMove(Movable.Direction direction) {
        Message message = new Message(Topic.MOVE, new DirectionMsg(direction));
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(message);
            log.info(json);
            session.getRemote().sendString(json);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при преобразовании объекта Message в json", e);
        } catch (IOException e) {
            log.error("Ошибка при отправке сообщения {}", json);
        }

    }

    public void sendPlantBomb() {
        Message message = new Message(Topic.PLANT_BOMB, "");
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(message);
            log.info(json);
            session.getRemote().sendString(json);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при преобразовании объекта Message в json", e);
        } catch (IOException e) {
            log.error("Ошибка при отправке сообщения {}", json);
        }
    }

//    public static void main(String[] args) {
//
//        WebSocketClient client = new WebSocketClient();
//        final URI uri = URI.create("ws://localhost:8090/events/");
//
//        try {
//            try {
//                client.start();
//                // The socket that receives events
//                EventHandler socket = new EventHandler();
//                // Attempt Connect
//                Future<Session> fut = client.connect(socket, uri);
//                // Wait for Connect
//                Session session = fut.get();
//                // Send a message
//
//                session.getRemote().sendString("Hello");
//                // Close session
//                session.close();
//            } finally {
//                client.stop();
//            }
//        } catch (Throwable t) {
//            t.printStackTrace(System.err);
//        }
//    }
}

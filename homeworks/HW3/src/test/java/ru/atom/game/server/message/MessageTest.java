package ru.atom.game.server.message;

import org.junit.Test;
import ru.atom.game.server.geometry.Point;
import ru.atom.game.server.model.Fire;
import ru.atom.game.server.model.GameSession;
import ru.atom.game.server.model.Movable;
import ru.atom.game.server.util.JsonHelper;

import java.util.Objects;

import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void testHelloParse() {
        Message message = new Message(Topic.HELLO, "Test");
        String json = JsonHelper.toJson(message);
        Message parsedMessage = JsonHelper.fromJson(json, Message.class);
        assertTrue(message.equals(parsedMessage));
    }

    @Test
    public void testPlantBombParse() {
        Message message = new Message(Topic.PLANT_BOMB, "");
        String json = JsonHelper.toJson(message);
        Message parsedMessage = JsonHelper.fromJson(json, Message.class);
        assertTrue(message.equals(parsedMessage));
    }

    @Test
    public void testMoveParseToJson() {
        DirectionMsg directionMsg = new DirectionMsg(Movable.Direction.DOWN);
        Message message = new Message(Topic.MOVE, directionMsg);
        String json = JsonHelper.toJson(message);
        System.out.println(json);
        Message parsedMessage = JsonHelper.fromJson(json, Message.class);
        assertTrue(message.equals(parsedMessage));

        String directionJson = parsedMessage.getData();
        System.out.println(directionJson);
        DirectionMsg parseDirectionMsg = JsonHelper.fromJson(directionJson, DirectionMsg.class);
        assertTrue(directionMsg.equals(parseDirectionMsg));
    }

    @Test
    public void testMoveParseFromJson() {
        String json =  "{\"topic\":\"MOVE\",\"data\":{\"direction\":\"UP\"}}";
        Message parsedMessage = JsonHelper.fromJson(json, Message.class);

        String directionJson = parsedMessage.getData();
        System.out.println(directionJson);

        DirectionMsg parseDirectionMsg = JsonHelper.fromJson(directionJson, DirectionMsg.class);

        assertTrue(parseDirectionMsg.getDirection() == Movable.Direction.UP);
    }

    @Test
    public void testReplicaToJson() {
        GameSession session = new GameSession();
        session.addGameObject(new Fire(new Point(90,90)));

        Message parsedMessage = new Message(Topic.REPLICA, session.getGameObjects());
        System.out.println(JsonHelper.toJson(parsedMessage));
    }


}
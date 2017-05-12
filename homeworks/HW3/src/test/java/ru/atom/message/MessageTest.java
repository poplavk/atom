package ru.atom.message;

import org.junit.Test;
import ru.atom.geometry.Point;
import ru.atom.model.Bomb;
import ru.atom.model.Fire;
import ru.atom.model.GameSession;
import ru.atom.model.Movable;
import ru.atom.util.JsonHelper;

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
    public void testMoveParse() {
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
    public void testMoveDownParse() {
        String json =  "{\"topic\":\"MOVE\",\"data\":{\"direction\":\"UP\"}}";
        Message parsedMessage = JsonHelper.fromJson(json, Message.class);

        String directionJson = parsedMessage.getData();
        System.out.println(directionJson);

        DirectionMsg parseDirectionMsg = JsonHelper.fromJson(directionJson, DirectionMsg.class);

        assertTrue(parseDirectionMsg.getDirection() == Movable.Direction.UP);
    }

    @Test
    public void testReplica() {
        GameSession session = new GameSession();
        session.addGameObject(new Fire(new Point(90,90)));

        String json = JsonHelper.toJson(session.getGameObjects());
        Message parsedMessage = new Message(Topic.REPLICA, json);
        System.out.println(json);
        System.out.println(JsonHelper.toJson(parsedMessage));
        assertTrue(Objects.equals(parsedMessage.getData(), "[{\"position\":{\"x\":90,\"y\":90},\"type\":\"Fire\",\"dead\":false}]"));

        assertTrue(Objects.equals(JsonHelper.toJson(parsedMessage),
                "{\"topic\":\"REPLICA\",\"data\":[{\\\"position\\\":{\\\"x\\\":90,\\\"y\\\":90},\\\"type\\\":\\\"Fire\\\",\\\"dead\\\":false}]}"));
    }


}
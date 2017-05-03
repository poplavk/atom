package ru.atom;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.atom.event.EventClient;
import ru.atom.model.Movable.Direction;


/**
 * Created by mkai on 4/30/17.
 */
public class EventClientTest {
    private EventClient eventClient = new EventClient();

    @Before
    public void start() {
        eventClient.connect();
    }

//    @Test
//    public void testHello() {
//        eventClient.sendHello("kolobok-killer");
//        Assert.assertTrue(true == true);
//    }

    @Test
    public void testStartGame() {
        eventClient.sendHello("1");
        eventClient.sendHello("2");
        eventClient.sendHello("3");
        eventClient.sendHello("kolobok-killer");

    }

    @Test
    public void testMove() {
        eventClient.sendMove(Direction.UP);
        Assert.assertTrue(true == true);
    }

    @Test
    public void testPlantBomb() {
        eventClient.sendPlantBomb();
        Assert.assertTrue(true == true);
    }
}
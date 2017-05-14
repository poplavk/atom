package ru.atom;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.atom.game.server.event.EventClient;
import ru.atom.game.server.model.Movable.Direction;


/**
 * Created by mkai on 4/30/17.
 */
public class EventClientTest {
    private EventClient eventClient = new EventClient();

    @Before
    public void start() {
        eventClient.connect();
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
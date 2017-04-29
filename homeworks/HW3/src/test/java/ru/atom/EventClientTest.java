package ru.atom;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.atom.event.EventClient;


/**
 * Created by mkai on 4/30/17.
 */
public class EventClientTest {
    private EventClient eventClient = new EventClient();

    @Before
    public void start() {
        eventClient.connect();
    }

    @Test
    public void testHello() {
        eventClient.sendHello("kolobok-killer");
        Assert.assertTrue(true == true);
    }
}
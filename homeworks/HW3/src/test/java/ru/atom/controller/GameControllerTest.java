package ru.atom.controller;

import org.junit.Before;
import org.junit.Test;
import ru.atom.event.EventClient;

import static org.junit.Assert.*;

/**
 * Created by dmbragin on 5/3/17.
 */
public class GameControllerTest {
    private EventClient eventClient = new EventClient();

    @Before
    public void start() {
        eventClient.connect();
    }

    @Test
    public void addPlayerAndStartGame() throws Exception {
        eventClient.sendHello("1");
        eventClient.sendHello("2");
        eventClient.sendHello("3");
        eventClient.sendHello("kolobok-killer");

    }

    @Test
    public void onMsgHandlerMove() throws Exception {

    }

}
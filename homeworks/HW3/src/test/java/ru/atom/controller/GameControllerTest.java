package ru.atom.controller;

import org.junit.Before;
import org.junit.Test;
import ru.atom.event.EventClient;
import ru.atom.model.Movable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dmbragin on 5/3/17.
 */
public class GameControllerTest {
    private EventClient eventClient = new EventClient();
    private List<EventClient> eventClients = new ArrayList<>();
    private boolean firstRun = true;


    @Before
    public void start() {
        if (firstRun) {

//            eventClient.connect();
            int i = 0;
            while (i < 4) {
                i++;
                EventClient client = new EventClient();
                client.connect();
                eventClients.add(client);
                client.sendHello(String.valueOf(i));

            }
            firstRun = false;
        }
    }

    @Test
    public void addPlayerAndStartGame() throws Exception {
        for (int i=0; i < 4; i++ ) {
            eventClients.get(i).sendHello(String.valueOf(i));
        }
    }

    @Test
    public void onMsgHandlerMove() throws Exception {
        for (int j=0; j < 4; j++ ) {

            eventClients.get(j).sendMove(Movable.Direction.DOWN);
        }
    }

    @Test
    public void onMsgHandlerPlantBomb() throws Exception {
        for (int j=0; j < 4; j++ ) {

            eventClients.get(j).sendPlantBomb();
        }
    }


}
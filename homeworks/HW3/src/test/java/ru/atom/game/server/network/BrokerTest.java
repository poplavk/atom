package ru.atom.game.server.network;

import org.junit.Before;
import org.junit.Test;
import ru.atom.game.server.event.EventClient;
import ru.atom.game.server.model.Movable;

import java.util.ArrayList;
import java.util.List;

public class BrokerTest {
    private EventClient eventClient = new EventClient();
    private List<EventClient> eventClients = new ArrayList<>();
    private boolean firstRun = true;
    private final int count = 4;

    @Before
    public void start() {
        if (firstRun) {

            for (int i = 0; i < count; i++) {
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
        for (int i = 0; i < 4; i++) {
            eventClients.get(i).sendHello(String.valueOf(i));
        }
    }

    @Test
    public void onMsgHandlerMove() throws Exception {
        for (int j = 0; j < 4; j++) {
            eventClients.get(j).sendMove(Movable.Direction.DOWN);
            eventClients.get(j).sendMove(Movable.Direction.RIGHT);
        }
    }

    @Test
    public void onMsgHandlerPlantBomb() throws Exception {
        for (int j = 0; j < count; j++) {
            eventClients.get(j).sendPlantBomb();
        }
    }
}
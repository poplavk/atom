package ru.atom.auth.server.mm;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.auth.server.base.Match;
import ru.atom.auth.server.service.MatchMakerService;

import java.util.Arrays;

public class GameSession {
    private static final Logger logger = LogManager.getLogger(GameSession.class);

    public static final int PLAYERS_IN_GAME = 4;

    private final Connection[] connections;

    private final Integer id;



    public GameSession(Connection[] connections) {
        this.id = MatchMakerService.saveMatch(new Match());
        this.connections = connections;
        sendIdToConnections();
    }

    public long getId() {
        return id;
    }

    public void sendIdToConnections() {
        for (Connection connection: connections) {
            connection.setSessionId(id);
        }
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "connections=" + Arrays.toString(connections) +
                ", id=" + id +
                '}';
    }
}

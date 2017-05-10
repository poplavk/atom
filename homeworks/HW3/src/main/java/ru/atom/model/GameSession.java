package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.dbhackaton.server.base.Match;
import ru.atom.dbhackaton.server.mm.Connection;
import ru.atom.dbhackaton.server.service.MatchMakerService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameSession implements Tickable, Runnable {
    private static final Logger logger = LogManager.getLogger(ru.atom.dbhackaton.server.mm.GameSession.class);
//    private static AtomicLong idGenerator = new AtomicLong();
    public static final int PLAYERS_IN_GAME = 4;
    private final Connection[] connections;
    private final Integer id;
    private static final Logger log = LogManager.getLogger(GameSession.class);
    private List<GameObject> gameObjects = new ArrayList<>();
    private static int lastId = 0;


    public static int getNextId() {
        return lastId++;
    }

    public List<GameObject> getGameObjects() {
        return new ArrayList<>(gameObjects);
    }

    public synchronized void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        log.info("add new game object {}", gameObject.getClass().toString());
    }

    public GameSession() {
        this.id = MatchMakerService.saveMatch(new Match());
        this.connections = new Connection[PLAYERS_IN_GAME];
        sendIdToConnections();
    }

    public long getId() {
        return id;
    }

    public void sendIdToConnections(){
        for (Connection connection: connections) {
//            connection.setSessionId(id);
        }
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "connections=" + Arrays.toString(connections) +
                ", id=" + id +
                '}';
    }

    @Override
    public synchronized void tick(long elapsed) {
        log.info("tick");
        ArrayList<Temporary> dead = new ArrayList<>();
        ArrayList<GameObject> newObjects = new ArrayList<>();
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Tickable) {
                ((Tickable) gameObject).tick(elapsed);
            }
            if (gameObject instanceof Temporary && ((Temporary) gameObject).isDead()) {
                dead.add((Temporary) gameObject);
                if (gameObject instanceof Bomb) {
                    newObjects.addAll( ((Bomb) gameObject).getBlast());
                }
            }
        }
        gameObjects.removeAll(dead);
        gameObjects.addAll(newObjects);
    }

    @Override
    public void run() {

    }
}

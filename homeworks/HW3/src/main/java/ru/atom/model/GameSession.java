package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.dbhackaton.server.base.Match;
import ru.atom.dbhackaton.server.mm.Connection;
import ru.atom.dbhackaton.server.service.MatchMakerService;
import ru.atom.geometry.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GameSession implements Tickable {
    private static final Logger logger = LogManager.getLogger(ru.atom.dbhackaton.server.mm.GameSession.class);

    public static final int TILE_SIZE = 32;
    public static final int TILES_X = 17;
    public static final int TILES_Y = 13;
//    private static AtomicLong idGenerator = new AtomicLong();

    private static final Logger log = LogManager.getLogger(GameSession.class);
    private List<GameObject> gameObjects = new ArrayList<>();

//    private ConcurrentHashMap<Point, GameObject> canvas = new ConcurrentHashMap<>();

    private static int lastId = 0;

    //TODO мне ненравится
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


    private void generateMap() {
//        gameObjects.add(new Wall(new Point(10, 10), Wall.BrickType.UNBREACABLE));
        //внешние стенки
        for (int i = 0; i < TILES_Y; i++) {
            gameObjects.add(new Wall(new Point(0, i), Wall.BrickType.UNBREACABLE));
            gameObjects.add(new Wall(new Point(TILES_X - 1, i), Wall.BrickType.UNBREACABLE));

        }
        for (int i = 0; i < TILES_X; i++) {
            gameObjects.add(new Wall(new Point(i, 0), Wall.BrickType.UNBREACABLE));
            gameObjects.add(new Wall(new Point(i, TILES_Y - 1), Wall.BrickType.UNBREACABLE));
        }
        // TODO add map generator
    }


    public GameSession() {
        generateMap();
    }

    @Override
    public synchronized void tick(long elapsed) {
        log.info("tick");
        ArrayList<Temporary> dead = new ArrayList<>();
        ArrayList<GameObject> newObjects = new ArrayList<>();

        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Girl) {
                Bomb bomb = ((Girl) gameObject).getBombForPlant();
                if (bomb != null) {
                    newObjects.add(bomb);
                }
            }
            if (gameObject instanceof Tickable) {
                ((Tickable) gameObject).tick(elapsed);
            }
            if (gameObject instanceof Temporary && ((Temporary) gameObject).isDead()) {
                dead.add((Temporary) gameObject);
                if (gameObject instanceof Bomb) {
                    newObjects.addAll(((Bomb) gameObject).getBlast());
                }
            }
        }

        gameObjects.removeAll(dead);
        gameObjects.addAll(newObjects);
    }
}

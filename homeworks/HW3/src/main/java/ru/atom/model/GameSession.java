package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;

import java.util.ArrayList;
import java.util.List;

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
        generateWalls();
        generateWoods();


    }

    private void generateWoods() {
        for (int y = 4; y < 9; y++) {
            for (int x = 1; x < 5; x++) {
                addWood(x, y);
            }
            for (int x = 12; x < 16; x++) {
                addWood(x, y);
            }
        }
        for (int x = 4; x < 13; x++) {
            for (int y = 1; y < 3; y++) {
                addWood(x, y);
            }
            for (int y = 10; y < 12; y++) {
                addWood(x, y);
            }
        }
        for (int x = 6; x < 11; x++) {
            for (int y = 5; y < 8; y++) {
                addWood(x, y);
            }
        }
    }

    public void addWall(int x, int y) {
        gameObjects.add(new Tile(new Point(x, y), Tile.TileType.Wall));
    }

    public void addWood(int x, int y) {
        gameObjects.add(new Tile(new Point(x, y), Tile.TileType.Wood));
    }

    public void generateWalls() {
        for (int y = 0; y < TILES_Y; y++) {
            addWall(0, y);
            addWall(TILES_X - 1, y);

        }
        for (int x = 0; x < TILES_X; x++) {
            addWall(x, 0);
            addWall(x, TILES_Y - 1);
        }

        addWall(2, 2);
        addWall(2, 3);
        addWall(3, 2);

        addWall(2, 10);
        addWall(2, 9);
        addWall(3, 10);

        addWall(14, 10);
        addWall(13, 10);
        addWall(14, 9);

        addWall(14, 2);
        addWall(13, 2);
        addWall(14, 3);

        addWall(8, 3);
        addWall(8, 4);

        addWall(8, 8);
        addWall(8, 9);

        addWall(5, 7);
        addWall(5, 6);
        addWall(5, 5);

        addWall(11, 7);
        addWall(11, 6);
        addWall(11, 5);

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

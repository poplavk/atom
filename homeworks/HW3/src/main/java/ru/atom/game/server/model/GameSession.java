package ru.atom.game.server.model;

import com.sun.jna.platform.win32.WinUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.server.geometry.Bar;
import ru.atom.game.server.geometry.Point;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameSession implements Tickable {
    private static final Logger logger = LogManager.getLogger(GameSession.class);

    public static final int TILE_SIZE = 32;
    public static final int TILES_X = 17;
    public static final int TILES_Y = 13;

    private static final Logger log = LogManager.getLogger(GameSession.class);
    private List<GameObject> gameObjects = new ArrayList<>();

    private static int lastId = 0;

    public GameSession() {
        generateMap();
    }

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

    @Override
    public synchronized void tick(long elapsed) {
        log.info("tick");
        ArrayList<Temporary> dead = new ArrayList<>();
        ArrayList<Mortal> kill = new ArrayList<>();
        ArrayList<GameObject> newObjects = new ArrayList<>();

        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Fire) {
                Fire fire = (Fire) gameObject;
                kill.addAll(boom(fire));
//                if (newDead.size() != 0) {
//                    dead.addAll(newDead);
//                }
            }
            if (gameObject instanceof Girl) {
                Girl girl = (Girl) gameObject;
                Bomb bomb = (girl).getBombForPlant();
                if (bomb != null) {
                    newObjects.add(bomb);
                }
                if (!(girl.getNowDirection() == Movable.Direction.IDLE)) {
                    move(girl);
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
//            if (gameObject instanceof Mortal && ((Mortal) gameObject).isDead()){
//
//            }
        }

        gameObjects.removeAll(dead);
        gameObjects.removeAll(kill);
        gameObjects.addAll(newObjects);
    }

    public synchronized void move(Girl girl) {
//        girl.move(girl.getNowDirection());

        List<Bar> track = Girl.getTrack(girl.getSpeed(), girl.getBar(), girl.getNowDirection());
        List<GameObject> objects = gameObjects.stream().filter(gameObject -> gameObject instanceof Tile)
                .collect(Collectors.toList());
        for (Bar bar : track) {
            for (GameObject gameObject : objects) {
                if (!(gameObject instanceof Girl)) {
//                    if (gameObject instanceof Tile) {
                    double dist = getDistance(bar, ((Tile) gameObject).getBar());
                    if (dist <= 32) {
                        boolean colliding = bar.isColliding(((Tile) gameObject).getBar());
                        if (colliding) {
                            return;
                        }
                    }
//                    }
                }
            }
            girl.move(bar);
        }
        girl.setDirection(Movable.Direction.IDLE);
    }

    public ArrayList<Mortal> boom(Fire fire) {
        ArrayList<Mortal> dead = new ArrayList<>();
        Bar bar = fire.getBar();
        for (GameObject gameObject : gameObjects) {
            if (!(gameObject instanceof Fire)) {
                if (gameObject instanceof AbstractGameObject) {
                    AbstractGameObject abstractGameObject = (AbstractGameObject) gameObject;
                    double dist = getDistance(bar, abstractGameObject.getBar());
                    if (dist == 0) {
                        if ((abstractGameObject.getType().equals("Wall"))) {
                            break;
                        } else {
                            if (gameObject instanceof Mortal) {
                                ((Mortal) gameObject).kill();
                                dead.add((Mortal) gameObject);
                            }
                        }

                    }
                }
            }
        }
        return dead;
    }


    public static double getDistance(Bar bar1, Bar bar2) {
        double distanceX = Math.pow(bar1.getStartPoint().getX() - bar2.getStartPoint().getX(), 2);
        double distanceY = Math.pow(bar1.getStartPoint().getY() - bar2.getStartPoint().getY(), 2);
        return Math.sqrt(distanceX + distanceY);
    }


}

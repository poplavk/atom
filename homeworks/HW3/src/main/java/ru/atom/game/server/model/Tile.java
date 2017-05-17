package ru.atom.game.server.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.server.geometry.Point;

/**
 * Created by mkai on 3/6/17.
 */
public class Tile extends AbstractGameObject implements Mortal {
    private static final Logger logger = LogManager.getLogger(Tile.class);
    private transient boolean isDead = false;

    @Override
    public boolean isDead() {
        return isDead;
    }

    public enum TileType {
        Wall,
        Wood
    }

    public Tile(Point point, TileType tileType) {
        super(point, tileType.toString());
        logger.info("new Tile! id = {} x = {} y = {} Type = {}", getId(), point.getX(), point.getY());

    }

    public void kill() {
        isDead = true;
    }

}
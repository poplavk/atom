package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;

/**
 * Created by mkai on 3/6/17.
 */
public class Tile extends AbstractGameObject {
    private static final Logger logger = LogManager.getLogger(Tile.class);

    public enum TileType{
        Wall,
        Wood
    }

    public Tile(Point point, TileType tileType) {
        super(point, tileType.toString());
        logger.info("new Tile! id = {} x = {} y = {} Type = {}", getId(), point.getX(), point.getY());

    }

}

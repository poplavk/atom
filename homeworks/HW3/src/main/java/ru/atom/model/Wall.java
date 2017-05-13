package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;

/**
 * Created by mkai on 3/6/17.
 */
public class Wall extends AbstractGameObject {
    private static final Logger logger = LogManager.getLogger(Wall.class);

    public enum BrickType {
        BREAKABLE,
        UNBREACABLE,
    }

    private transient BrickType wallType;

    public Wall(Point point, BrickType type) {
        super(point, "Wall");
        this.wallType = type;
        logger.info("new Wall! id = {} x = {} y = {} Type = {}", getId(), point.getX(), point.getY(), type);

    }

}

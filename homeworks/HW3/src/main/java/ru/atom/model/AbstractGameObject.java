package ru.atom.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;


public abstract class AbstractGameObject implements Positionable {
    private static final Logger logger = LogManager.getLogger(GameSession.class);

    private final int id;
    private Point position;
    private final String className;

    @JsonCreator
    public AbstractGameObject(@JsonProperty("position") Point point, @JsonProperty("type") String clsName) {
        if ( point.getX() < 0 || point.getY() < 0) {
            logger.error("GameObjects x, y must be >= 0!");
            throw new IllegalArgumentException();
        }

        this.id = GameSession.getNextId();
        this.position = point;
        this.className = clsName;
    }

    public int getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position = new Point(x, y);
    }

    public void setPosition(Point newPosition) {
        position = newPosition;
    }

    public String getClassName() {
        return className;
    }
}

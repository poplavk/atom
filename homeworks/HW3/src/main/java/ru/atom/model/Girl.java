package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;


public class Girl extends AbstractGameObject implements Movable {
    private static final Logger logger = LogManager.getLogger(Girl.class);

    // TODO: 4/30/17 добавить имя игрока?

    private int speed;
    private int bombCapacity;
    private int rangeOfExplosion;
    private long passedTimeMillis;


    public Girl(Point point) {
        super(point, "Girl");
        bombCapacity = 1;
        rangeOfExplosion = 3;
        speed = 1;
        logger.info("new Girl! id = {} x = {} y = {} speed = {}", getId(), point.getX(), point.getY(), speed);

    }

    public Girl(Point point, int speed, int bombCapacity, int rangeOfExplosion) {
        super(point, "Girl");
        if (speed <= 0) {
            logger.error("Girls speed must be > 0!");
            throw new IllegalArgumentException();
        }
        this.speed = speed;
        this.bombCapacity = bombCapacity;
        this.rangeOfExplosion = rangeOfExplosion;
        logger.info("new Girl! id = {} x = {} y = {} speed = {}", getId(), point.getX(), point.getY(), speed);
    }

    @Override
    public void tick(long elapsed) {
        passedTimeMillis += elapsed;
    }

    @Override
    public Point move(Direction direction) {
        Point newPosition;
        switch (direction) {
            case UP:
                newPosition = new Point(getPosition().getX(), getPosition().getY() + speed);
                moveLog("UP", getPosition().getX(), getPosition().getY(),
                        newPosition.getX(), newPosition.getY());
                setPosition(newPosition);
                return newPosition;
            case DOWN:
                newPosition = new Point(getPosition().getX(), getPosition().getY() - speed);
                moveLog("DOWN", getPosition().getX(), getPosition().getY(),
                        newPosition.getX(), newPosition.getY());
                setPosition(newPosition);
                return newPosition;
            case RIGHT:
                newPosition = new Point(getPosition().getX() + speed, getPosition().getY());
                moveLog("RIGHT", getPosition().getX(), getPosition().getY(),
                        newPosition.getX(), newPosition.getY());
                setPosition(newPosition);
                return newPosition;
            case LEFT:
                newPosition = new Point(getPosition().getX() - speed, getPosition().getY());
                moveLog("LEFT", getPosition().getX(), getPosition().getY(),
                        newPosition.getX(), newPosition.getY());
                setPosition(newPosition);
                return newPosition;
            case IDLE:
                return getPosition();
            default:
                return getPosition();
        }
    }

    public void moveLog(String direction, int oldX, int oldY, int x, int y) {
        logger.info("Girl id = {} moved {}! ({}, {}) -> ({}, {})",
                getId(), direction, oldX, oldY, x, y);
    }
}

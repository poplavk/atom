package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;


public class Girl extends AbstractGameObject implements Movable {
    private static final Logger logger = LogManager.getLogger(Girl.class);

    // TODO: 4/30/17 добавить имя игрока?

    private transient int  speed;
    private transient int bombCapacity;
    private transient int rangeOfExplosion;
    private transient long passedTimeMillis;
    private transient boolean wasMovedOnTick = false;

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


    public void setNewPosition(Point newPosition, String direction){
        if (newPosition != null) {
            moveLog(direction, getPosition().getX(), getPosition().getY(),
                    newPosition.getX(), newPosition.getY());
            setPosition(newPosition);
        }
    }


    @Override
    public Point move(Direction direction) {
        // TODO maybe add queue for actions
        if (wasMovedOnTick)
            return getPosition();
        wasMovedOnTick = true;

        Point newPosition = null;
        String directionString = "IDLE";
        switch (direction) {
            case UP:
                newPosition = new Point(getPosition().getX(), getPosition().getY() + speed);
                directionString = "UP";
                break;
            case DOWN:
                newPosition = new Point(getPosition().getX(), getPosition().getY() - speed);
                directionString = "DOWN";
                break;
            case RIGHT:
                newPosition = new Point(getPosition().getX() + speed, getPosition().getY());
                directionString = "RIGHT";
                break;
            case LEFT:
                newPosition = new Point(getPosition().getX() - speed, getPosition().getY());
                directionString = "LEFT";
                break;
            case IDLE:
            default:
                break;
        }
        setNewPosition(newPosition, directionString);
        return getPosition();
    }

    public Bomb plantBomb() {
        //TODO add work with bomb capacity
        return new Bomb(getPosition());
    }


    public void moveLog(String direction, int oldX, int oldY, int x, int y) {
        logger.info("Girl id = {} moved {}! ({}, {}) -> ({}, {})",
                getId(), direction, oldX, oldY, x, y);
    }
}

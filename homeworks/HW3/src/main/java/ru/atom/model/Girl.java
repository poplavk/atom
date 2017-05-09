package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;


public class Girl extends AbstractGameObject implements Movable {
    private static final Logger logger = LogManager.getLogger(Girl.class);

    // TODO: 4/30/17 добавить имя игрока?

    private transient int  speed = 1;
    private transient int bombCapacity = 1;
    private transient int rangeOfExplosion = 3;
    private transient long passedTimeMillis;
    private transient boolean wasMovedOnTick = false;

    public Girl(Point point) {
        super(point, "Girl");
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
    public synchronized Point move(Direction direction) {
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
        bombCapacity--;
        return new Bomb(getPosition(), this, rangeOfExplosion);
    }

    public void increaseBombCapacity() {
        bombCapacity++;
    }

    public void moveLog(String direction, int oldX, int oldY, int x, int y) {
        logger.info("Girl id = {} moved {}! ({}, {}) -> ({}, {})",
                getId(), direction, oldX, oldY, x, y);
    }
}

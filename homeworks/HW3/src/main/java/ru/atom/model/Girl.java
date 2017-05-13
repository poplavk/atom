package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;


public class Girl extends AbstractGameObject implements Movable {
    private static final Logger logger = LogManager.getLogger(Girl.class);

    // TODO: 4/30/17 добавить имя игрока?

    private transient int  speed = 1;
    private transient int bombCapacity = 1;
    private transient int rangeOfExplosion = 1;
    private transient long passedTimeMillis;

    //TODO  переделать это на зависимость от времени
    private transient boolean wasActedOnTick = false;
    private transient Bomb bombForPlant = null;

    public Girl(Point point) {
        super(point, "Pawn");
        logger.info("new Girl! id = {} x = {} y = {} speed = {}", getId(), point.getX(), point.getY(), speed);
    }

    @Override
    public void tick(long elapsed) {
        wasActedOnTick = false;
        bombForPlant = null;
        passedTimeMillis += elapsed;
    }


    private void setNewPosition(Point newPosition, String direction){
        if (newPosition != null) {
            moveLog(direction, getPosition().getX(), getPosition().getY(),
                    newPosition.getX(), newPosition.getY());
            setPosition(newPosition);
        }
    }


    @Override
    public synchronized Point move(Direction direction) {
        // TODO maybe add queue for actions
        if (wasActedOnTick)
            return getPosition();
        wasActedOnTick = true;

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

    public synchronized void plantBomb() {
        if (wasActedOnTick || bombCapacity <= 0) {
            return;
        }
        wasActedOnTick = true;
        bombCapacity--;
        int x = getPosition().getX() / 32;
        int y = getPosition().getY() / 32;
        if(getPosition().getX() % 32 > 16) {
            x++;
        }
        if(getPosition().getY() % 32 > 16) {
            y++;
        }
        Point bombPosition = new Point(x * 32, y * 32);
        bombForPlant = new Bomb(bombPosition, this, rangeOfExplosion);
    }

    public synchronized void increaseBombCapacity() {
        bombCapacity++;
    }

    private void moveLog(String direction, int oldX, int oldY, int x, int y) {
        logger.info("Girl id = {} moved {}! ({}, {}) -> ({}, {})",
                getId(), direction, oldX, oldY, x, y);
    }

    public Bomb getBombForPlant() {
        return bombForPlant;
    }
}

package ru.atom.game.server.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.server.geometry.Bar;
import ru.atom.game.server.geometry.Point;

import java.util.ArrayList;
import java.util.List;


public class Girl extends AbstractGameObject implements Movable, Mortal {
    private static final Logger logger = LogManager.getLogger(Girl.class);

    // TODO: 4/30/17 добавить имя игрока?

    private transient int speed = 1;
    private transient int bombCapacity = 1;
    private transient int rangeOfExplosion = 1;
    private transient long passedTimeMillis;
    private transient boolean isDead = false;

    //TODO  переделать это на зависимость от времени
    private transient boolean wasActedOnTick = false;
    private transient Bomb bombForPlant = null;

    private transient Direction nowDirection = Direction.IDLE;

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


    private void setNewPosition(Point newPosition) {
        if (newPosition != null) {
            moveLog(nowDirection, getPosition().getX(), getPosition().getY(),
                    newPosition.getX(), newPosition.getY());
            setPosition(newPosition);
        }
    }

    public synchronized void move(Bar bar) {
        setNewPosition(bar.getStartPoint());
    }

    @Override
    public synchronized Point move(Direction direction) {
        // TODO maybe add queue for actions
        if (wasActedOnTick)
            return getPosition();
        wasActedOnTick = true;
        int speed = 1;

        Point newPosition = null;
        switch (nowDirection) {
            case UP:
                newPosition = new Point(getPosition().getX(), getPosition().getY() + speed);
//                nowDirection = Direction.UP;
                break;
            case DOWN:
                newPosition = new Point(getPosition().getX(), getPosition().getY() - speed);
//                nowDirection = Direction.DOWN;
                break;
            case RIGHT:
                newPosition = new Point(getPosition().getX() + speed, getPosition().getY());
//                nowDirection = Direction.RIGHT;
                break;
            case LEFT:
                newPosition = new Point(getPosition().getX() - speed, getPosition().getY());
//                nowDirection = Direction.LEFT;
                break;
            case IDLE:
                break;
            default:
                break;
        }
        setNewPosition(newPosition);
        return getPosition();
    }

    public synchronized void plantBomb() {
        if (wasActedOnTick || bombCapacity <= 0) {
            return;
        }
        wasActedOnTick = true;
        bombCapacity--;
        int pointX = getPosition().getX() / 32;
        int pointY = getPosition().getY() / 32;
        if (getPosition().getX() % 32 > 16) {
            pointX++;
        }
        if (getPosition().getY() % 32 > 16) {
            pointY++;
        }
        Point bombPosition = new Point(pointX * 32, pointY * 32);
        bombForPlant = new Bomb(bombPosition, this, rangeOfExplosion);
    }

    public synchronized void increaseBombCapacity() {
        bombCapacity++;
    }

    private void moveLog(Direction direction, int oldX, int oldY, int x, int y) {
        logger.error("Girl id = {} moved {}! ({}, {}) -> ({}, {})",
                getId(), direction, oldX, oldY, x, y);
    }

    public Bomb getBombForPlant() {
        return bombForPlant;
    }

    public int getSpeed() {
        return speed;
    }

    public static List<Bar> getTrack(int speed, Bar position, Movable.Direction direction) {
        List<Bar> track = new ArrayList<>();
        Bar bar = position;
        for (int i = 1; i <= speed; i++) {
            switch (direction) {
                case UP:
                    bar = Bar.getUpBar(bar);
                    track.add(bar);
                    break;
                case DOWN:
                    bar = Bar.getDownBar(bar);
                    track.add(bar);
                    break;
                case RIGHT:
                    bar = Bar.getRightBar(bar);
                    track.add(bar);
                    break;
                case LEFT:
                    bar = Bar.getLeftBar(bar);
                    track.add(bar);
                    break;
                case IDLE:
                    break;
                default:
                    break;
            }
        }
        return track;

    }


    public synchronized void setDirection(Direction direction) {
        this.nowDirection = direction;
        if (!isDead) {
            logger.error("set direction {}", direction);
        }
    }

    public Direction getNowDirection() {
        return nowDirection;
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    public synchronized void kill() {

        logger.info("Girl {} kill!", super.getId());
        isDead = true;
    }

    public long getPassedTimeMillis() {
        return passedTimeMillis;
    }
}

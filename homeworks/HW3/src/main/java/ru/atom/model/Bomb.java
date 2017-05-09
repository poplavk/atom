package ru.atom.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;

public class Bomb extends AbstractGameObject implements Temporary {
    private static final Logger logger = LogManager.getLogger(Bomb.class);
    private final transient int LIFE_TIME = 2000;

    private transient int range;
    private transient long passedTimeMillis;
    private transient Girl owner;

    public Bomb(Point point, Girl owner, int range) {
        super(point, "Bomb");
        this.range = range;
        this.owner = owner;
        logger.info("new Bomb! id = {} x = {} y = {}", getId(), point.getX(), point.getY());

    }

    @Override
    public void tick(long elapsed) {
        passedTimeMillis += elapsed;
    }

    @Override
    public long getLifetimeMillis() {
        return LIFE_TIME;
    }

    @Override
    public boolean isDead() {
        boolean isDead = (passedTimeMillis > LIFE_TIME);
        if(isDead) {
            logger.info("BOOM! (BombId = {})", getId());
            owner.increaseBombCapacity();
            new Fire(this.getPosition(), range);
        }
        return isDead;
    }
}

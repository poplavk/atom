package ru.atom.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;

public class Bomb extends AbstractGameObject implements Temporary {
    private static final Logger logger = LogManager.getLogger(Bomb.class);

    private transient long lifeTimeMillis;
    private transient long passedTimeMillis;

    public Bomb(Point point, long lifeTimeMillis) {
        super(point, "Bomb");
        this.lifeTimeMillis = lifeTimeMillis;
        logger.info("new Bomb! id = {} x = {} y = {}", getId(), point.getX(), point.getY());

    }

    @Override
    public void tick(long elapsed) {
        passedTimeMillis += elapsed;
    }

    @Override
    public long getLifetimeMillis() {
        return lifeTimeMillis;
    }

    @Override
    public boolean isDead() {
        boolean isDead = (passedTimeMillis > lifeTimeMillis);
        logger.info("BOOM! (BombId = {})", getId());
        return isDead;
    }


}

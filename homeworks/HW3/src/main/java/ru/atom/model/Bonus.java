package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;


public class Bonus extends AbstractGameObject implements Positionable {
    private static final Logger logger = LogManager.getLogger(Bonus.class);

    public enum BonusType {
        SPEED,
        BOMB,
        FIRE
    }

    private BonusType type;

    public Bonus(Point point, BonusType type) {
        super(point, "Bonus");
        this.type = type;
        logger.info("new Bonus! id = {} x = {} y = {} type {}", getId(), point.getX(), point.getY(), type);
    }

}

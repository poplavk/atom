package ru.atom.model;

import ru.atom.geometry.Point;

/**
 * Created by allen on 04.05.2017.
 */
public class Fire extends AbstractGameObject implements Temporary, Tickable {
    private static final int LIFETIME = 300;
    private int elapsedTime = 0;
    private int range;

    public Fire(Point point, int range) {
        super(point,"Fire");
        this.range = range;
    }

    @Override
    public void tick(long elapsed) {
        elapsedTime += elapsed;
    }

    @Override
    public long getLifetimeMillis() {
        return LIFETIME;
    }

    @Override
    public boolean isDead() {
        return elapsedTime > LIFETIME;
    }
}
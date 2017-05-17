package ru.atom.game.server.model;

import ru.atom.game.server.geometry.Point;

/**
 * Created by allen on 04.05.2017.
 */
public class Fire extends AbstractGameObject implements Temporary, Tickable {
    private static final int LIFETIME = 190;
    private transient int elapsedTime = 0;

    public Fire(Point point) {
        super(point,"Fire");
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
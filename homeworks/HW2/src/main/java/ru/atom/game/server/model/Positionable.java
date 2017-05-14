package ru.atom.game.server.model;

import ru.atom.game.server.geometry.Point;

/**
 * GameObject that has coordinates
 * ^ Y
 * |
 * |
 * |
 * |          X
 * .---------->
 */
public interface Positionable extends GameObject {
    /**
     * @return Current position
     */
    Point getPosition();
}

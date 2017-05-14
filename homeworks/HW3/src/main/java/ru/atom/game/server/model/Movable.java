package ru.atom.game.server.model;

import ru.atom.game.server.geometry.Point;

/**
 * GameObject that can move during game
 */
public interface Movable extends Positionable, Tickable {
    /**
     * Tries to move entity towards specified direction
     * @return final position after movement
     */
    Point move(Direction direction);
    
    enum Direction {
        UP, DOWN, RIGHT, LEFT, IDLE
    }
}

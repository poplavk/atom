package ru.atom.message;

import ru.atom.model.Movable;

/**
 * Created by dmbragin on 5/3/17.
 */
public class DirectionMsg {
    private Movable.Direction direction;


    public DirectionMsg(Movable.Direction direction) {
        this.direction = direction;
    }

    public Movable.Direction getDirection() {
        return direction;
    }

    public void setDirection(Movable.Direction direction) {
        this.direction = direction;
    }
}

package ru.atom.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.atom.model.Movable;

/**
 * Created by dmbragin on 5/3/17.
 */
public class DirectionMsg {

    private Movable.Direction direction;

    @JsonCreator(mode=JsonCreator.Mode.PROPERTIES)
    public DirectionMsg(@JsonProperty("direction") String direction) {
        this.direction = Movable.Direction.valueOf(direction);
    }

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

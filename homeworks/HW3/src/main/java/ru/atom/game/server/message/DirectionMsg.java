package ru.atom.game.server.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.atom.game.server.model.Movable;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectionMsg that = (DirectionMsg) o;

        return direction == that.direction;
    }

    @Override
    public int hashCode() {
        return direction != null ? direction.hashCode() : 0;
    }
}

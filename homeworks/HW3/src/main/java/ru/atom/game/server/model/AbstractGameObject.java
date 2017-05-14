package ru.atom.game.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.server.geometry.Bar;
import ru.atom.game.server.geometry.Point;

import static ru.atom.game.server.model.GameSession.TILE_SIZE;


public abstract class AbstractGameObject implements Positionable {
    private static final Logger logger = LogManager.getLogger(GameSession.class);

    private final int id;
    private Point position;
    private final String type;
    private Bar bar;

    @JsonCreator
    public AbstractGameObject(@JsonProperty("position") Point point, @JsonProperty("type") String clsName) {
        this.id = GameSession.getNextId();
        this.position = point;
        this.type = clsName;
        int x = point.getX();
        int y = point.getY();
        this.bar = new Bar(x, y, x + TILE_SIZE, y + TILE_SIZE);
    }

    public int getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public Bar getBar() {
        return bar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractGameObject that = (AbstractGameObject) o;

        if (id != that.id) return false;
        if (position != null ? !position.equals(that.position) : that.position != null) return false;
        return type != null ? type.equals(that.type) : that.type == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public void setPosition(Point newPosition) {

        position = newPosition;

    }

    public String getType() {
        return type;
    }


}

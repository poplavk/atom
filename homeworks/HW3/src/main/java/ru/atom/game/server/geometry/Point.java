package ru.atom.game.server.geometry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Point implements Collider {
    private final int x;
    private final int y;

    @JsonCreator
    public Point(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public boolean isColliding(Collider other) {
        if (other instanceof Point) {
            return this.equals(other);
        }
        throw new NotImplementedException();
    }
}

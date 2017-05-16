package ru.atom.game.server.geometry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.server.model.GameSession;
import ru.atom.game.server.model.Tile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static ru.atom.game.server.model.GameSession.TILE_SIZE;

public class Bar implements Collider {
    private static final Logger logger = LogManager.getLogger(Bar.class);

    private final Point startPoint;
    private final Point endPoint;

    public Bar(int x1, int y1, int x2, int y2) {
        startPoint = new Point(Math.min(x1, x2), Math.min(y1, y2));
        endPoint = new Point(Math.max(x1, x2), Math.max(y1, y2));
    }


    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return isColliding((Collider) o);

        Bar bar = (Bar) o;

        return (this.startPoint.equals(bar.startPoint) && this.endPoint.equals(bar.endPoint));
    }

//    @Override
//    public boolean isColliding(Collider other) {
//        return /*isIncludes(other) */isIntersects(other);
//    }

//    public boolean isIncludes(Collider collider) {
//        if (collider instanceof Bar) {
//            Bar bar = (Bar) collider;
//            if (bar.startPoint.getX() > this.startPoint.getX() && bar.startPoint.getY() > this.startPoint.getY()
//                    && bar.endPoint.getX() < this.endPoint.getX() && bar.endPoint.getY() < this.endPoint.getY()) {
//                return true;
//            }
//            return false;
//        }
//        if (collider instanceof Point) {
//            Point point = (Point) collider;
//            if (point.getX() > this.startPoint.getX() && point.getX() < this.endPoint.getX()
//                    && point.getY() > this.startPoint.getY() && point.getY() < this.endPoint.getY()) {
//                return true;
//            }
//            return false;
//        }
//        throw new NotImplementedException();
//    }
//
//    public boolean isIntersects(Collider collider) {
//        if (collider instanceof Bar) {
//            Bar bar = (Bar) collider;
//            if (this.isIncludes(bar.startPoint) || this.isIncludes(bar.endPoint)) {
//                return true;
//            }
//            if (bar.startPoint.getX() >= this.startPoint.getX()
//                    && bar.endPoint.getX() <= this.endPoint.getX()
//                    && bar.startPoint.getY() <= this.startPoint.getY()
//                    && bar.endPoint.getY() >= this.endPoint.getY()) {
//                return true;
//            }
//            return false;
//        }
//        if (collider instanceof Point) {
//            return false;
//        }
//        throw new NotImplementedException();
//    }

    public static Bar getUpBar(Bar bar) {
        int x1 = bar.startPoint.getX();
        int y1 = bar.startPoint.getY() + 1;
        int x2 = bar.endPoint.getX();
        int y2 = bar.endPoint.getY() + 1;
        return new Bar(x1, y1, x2, y2);
    }

    public static Bar getDownBar(Bar bar) {
        int x1 = bar.startPoint.getX();
        int y1 = bar.startPoint.getY() - 1;
        int x2 = bar.endPoint.getY();
        int y2 = bar.endPoint.getY() - 1;
        return new Bar(x1, y1, x2, y2);
    }

    public static Bar getLeftBar(Bar bar) {
        int x1 = bar.startPoint.getX() - 1;
        int y1 = bar.startPoint.getY();
        int x2 = bar.endPoint.getX() - 1;
        int y2 = bar.endPoint.getY();
        return new Bar(x1, y1, x2, y2);
    }

    public static Bar getRightBar(Bar bar) {
        int x1 = bar.startPoint.getX() + 1;
        int y1 = bar.startPoint.getY();
        int x2 = bar.endPoint.getX() + 1;
        int y2 = bar.endPoint.getY();
        return new Bar(x1, y1, x2, y2);
    }

    @Override
    public boolean isColliding(Collider other) {
        if (other instanceof Bar) {

            int thisX0 = startPoint.getX();
            int thisY0 = startPoint.getY();
            int thisX1 = endPoint.getX();
            int thisY1 = endPoint.getY();

            int otherX0 = ((Bar) other).startPoint.getX();
            int otherY0 = ((Bar) other).startPoint.getY();
            int otherX1 = ((Bar) other).endPoint.getX();
            int otherY1 = ((Bar) other).endPoint.getY();
//            if (thisX0 == otherX0 && thisX1 == otherX1) {
//                    if (thisY0 > otherY0 && thisY1 < otherY1) {
//                    return true;
//                }
//            }
//            if (thisY0 == otherY0 && thisY1 == otherY1) {
//                if (thisX0 > otherX0 && thisX1 < otherX1) {
//                    return true;
//                }
//            }
            int partTile = TILE_SIZE / 2;
            if(
                       thisX0 - partTile < otherX0 + partTile
                    && thisY0 - partTile < otherY0 + partTile
                    && thisX0 + partTile > otherX0 - partTile
                    && thisY0 + partTile > otherY0 - partTile
                    || otherX0 - partTile < thisX0 + partTile
                    && otherY0 - partTile < thisY0 + partTile
                    && otherX0 + partTile > thisX0 - partTile
                    && otherY0 + partTile > thisY0 - partTile) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                '}';
    }
}

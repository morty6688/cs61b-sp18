package byog.Core;

import java.util.List;

import byog.TileEngine.TETile;

public class Connector {

    private Position goalPos;
    private Direction direction;

    public Connector(Position goalPos, Direction direction) {
        this.goalPos = goalPos;
        this.direction = direction;
    }

    public static void addConnectableDirection(List<Connector> l, TETile[][] world, TETile t,
            Direction d, Position p, int width, int height) {
        switch (d) {
            case RIGHT:
                if (p.getX() + 2 < width && world[p.getX() + 2][p.getY()].equals(t)) {
                    Connector conn = new Connector(new Position(p.getX() + 2, p.getY()), d);
                    l.add(conn);
                }
                break;
            case LEFT:
                if (p.getX() - 2 >= 0 && world[p.getX() - 2][p.getY()].equals(t)) {
                    Connector conn = new Connector(new Position(p.getX() - 2, p.getY()), d);
                    l.add(conn);
                }
                break;
            case UP:
                if (p.getY() + 2 < height && world[p.getX()][p.getY() + 2].equals(t)) {
                    Connector conn = new Connector(new Position(p.getX(), p.getY() + 2), d);
                    l.add(conn);
                }
                break;
            default:
                if (p.getY() - 2 >= 0 && world[p.getX()][p.getY() - 2].equals(t)) {
                    Connector conn = new Connector(new Position(p.getX(), p.getY() - 2), d);
                    l.add(conn);
                }
                break;
        }
    }

    public void connect(TETile[][] world, TETile t) {
        switch (direction) {
            case RIGHT:
                world[goalPos.getX() - 1][goalPos.getY()] = t;
                break;
            case LEFT:
                world[goalPos.getX() + 1][goalPos.getY()] = t;
                break;
            case UP:
                world[goalPos.getX()][goalPos.getY() - 1] = t;
                break;
            default:
                world[goalPos.getX()][goalPos.getY() + 1] = t;
                break;
        }
    }

    public Position getGoalPos() {
        return goalPos;
    }

}

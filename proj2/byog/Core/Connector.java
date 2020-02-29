package byog.Core;

import java.util.List;

import byog.TileEngine.TETile;

enum Direction {
    UP, DOWN, LEFT, RIGHT;
}

public class Connector {

    private Position goalPos;
    private Direction direct;

    public Connector(Position goalPos, Direction direct) {
        this.goalPos = goalPos;
        this.direct = direct;
    }

    public static void addConnectableDirection(List<Connector> l, TETile[][] world, TETile t, Direction d, Position p,
            int width, int height) {
        switch (d) {
            case RIGHT:
                if (p.x + 2 < width && world[p.x + 2][p.y] == t) {
                    Connector conn = new Connector(new Position(p.x + 2, p.y), d);
                    l.add(conn);
                }
                break;
            case LEFT:
                if (p.x - 2 >= 0 && world[p.x - 2][p.y] == t) {
                    Connector conn = new Connector(new Position(p.x - 2, p.y), d);
                    l.add(conn);
                }
                break;
            case UP:
                if (p.y + 2 < height && world[p.x][p.y + 2] == t) {
                    Connector conn = new Connector(new Position(p.x, p.y + 2), d);
                    l.add(conn);
                }
                break;
            case DOWN:
                if (p.y - 2 >= 0 && world[p.x][p.y - 2] == t) {
                    Connector conn = new Connector(new Position(p.x, p.y - 2), d);
                    l.add(conn);
                }
                break;
        }
    }

    public void connect(TETile[][] world, TETile t) {
        switch (direct) {
            case RIGHT:
                world[goalPos.x - 1][goalPos.y] = t;
                break;
            case LEFT:
                world[goalPos.x + 1][goalPos.y] = t;
                break;
            case UP:
                world[goalPos.x][goalPos.y - 1] = t;
                break;
            case DOWN:
                world[goalPos.x][goalPos.y + 1] = t;
                break;
        }
    }

    public Position getGoalPos() {
        return goalPos;
    }

    public void setGoalPos(Position goalPos) {
        this.goalPos = goalPos;
    }

    public Direction getDirect() {
        return direct;
    }

    public void setDirect(Direction direct) {
        this.direct = direct;
    }

}
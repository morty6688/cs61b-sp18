package byog.Core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Position implements Serializable {
    private static final long serialVersionUID = 3964068339997564469L;
    private int x;
    private int y;

    public Position() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * To judge if a tile is on the edge of world. (not all the edges can be
     * detected.)
     */
    public boolean isEdge(TETile[][] world, int width, int height) {
        if (!isTile(world, Tileset.WALL)) {
            return false;
        }
        // There is only one floor(pos) around the wall of edge.
        int num = 0;
        Position pos = new Position();
        for (Position p : getAroundPositions(width, height, false)) {
            if (p.isTile(world, Tileset.FLOOR)) {
                num++;
                pos = p;
            }
        }

        if (num == 1) {
            switch (getDirection(pos, width, height)) {
                case LEFT:
                    for (int i = this.x + 1; i < width; i++) {
                        Position p = new Position(i, this.y);
                        if (!p.isTile(world, Tileset.NOTHING)) {
                            return false;
                        }
                    }
                    break;
                case RIGHT:
                    for (int i = this.x - 1; i >= 0; i--) {
                        Position p = new Position(i, this.y);
                        if (!p.isTile(world, Tileset.NOTHING)) {
                            return false;
                        }
                    }
                    break;
                case DOWN:
                    for (int i = this.y + 1; i < height; i++) {
                        Position p = new Position(this.x, i);
                        if (!p.isTile(world, Tileset.NOTHING)) {
                            return false;
                        }
                    }
                    break;
                default:
                    for (int i = this.y - 1; i >= 0; i--) {
                        Position p = new Position(this.x, i);
                        if (!p.isTile(world, Tileset.NOTHING)) {
                            return false;
                        }
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    public Direction getDirection(Position other, int width, int height) {
        if (other.x > x) {
            return Direction.RIGHT;
        } else if (other.x < x) {
            return Direction.LEFT;
        } else if (other.y > y) {
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }

    public boolean isSolidWall(TETile[][] world, int width, int height) {
        if (!isTile(world, Tileset.WALL)) {
            return false;
        }
        for (Position p : getAroundPositions(width, height, true)) {
            if (!p.isTile(world, Tileset.WALL)) {
                return false;
            }
        }
        return true;
    }

    public void carveDeadEnd(TETile[][] world, int width, int height) {
        drawTile(world, Tileset.WALL);
        for (Position p : getAroundPositions(width, height, false)) {
            if (p.isDeadEnd(world, width, height)) {
                p.carveDeadEnd(world, width, height);
            }
        }
    }

    public boolean isDeadEnd(TETile[][] world, int width, int height) {
        if (!isTile(world, Tileset.FLOOR)) {
            return false;
        }
        int num = 0;
        for (Position p : getAroundPositions(width, height, false)) {
            if (p.isTile(world, Tileset.WALL)) {
                num++;
            }
        }
        if (num == 3) {
            return true;
        }
        return false;
    }

    public void drawTile(TETile[][] world, TETile t) {
        world[x][y] = t;
    }

    public boolean isTile(TETile[][] world, TETile t) {
        return world[x][y].equals(t);
    }

    private List<Position> getAroundPositions(int width, int height, boolean isAll) {
        List<Position> res = new ArrayList<>();
        if (x + 1 < width) {
            res.add(new Position(x + 1, y));
        }
        if (x - 1 >= 0) {
            res.add(new Position(x - 1, y));
        }
        if (y + 1 < height) {
            res.add(new Position(x, y + 1));
        }
        if (y - 1 >= 0) {
            res.add(new Position(x, y - 1));
        }
        if (isAll) {
            if (x + 1 < width && y + 1 < height) {
                res.add(new Position(x + 1, y + 1));
            }
            if (x - 1 >= 0 && y + 1 < height) {
                res.add(new Position(x - 1, y + 1));
            }
            if (x + 1 < width && y - 1 >= 0) {
                res.add(new Position(x + 1, y - 1));
            }
            if (x - 1 >= 0 && y - 1 >= 0) {
                res.add(new Position(x - 1, y - 1));
            }
        }
        return res;
    }

    public void drawInitialPerson(TETile[][] world, int width, int height) {
        for (Position p : getAroundPositions(width, height, false)) {
            if (p.isTile(world, Tileset.FLOOR)) {
                p.drawTile(world, Tileset.PLAYER);
                Player.setPos(p);
                return;
            }
        }
    }

}

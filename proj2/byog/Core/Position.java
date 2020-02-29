package byog.Core;

import java.util.ArrayList;
import java.util.List;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Position {
    public int x;
    public int y;

    public Position() {
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void carveExtraWalls(TETile[][] world) {
        
    }

    public boolean isSolidWall(TETile[][] world, int width, int height) {
        if (world[x][y] != Tileset.WALL) {
            return false;
        }
        for (Position p : getAroundPositions(width, height, true)) {
            if (!p.isWall(world)) {
                return false;
            }
        }
        return true;
    }

    public void carveDeadEnd(TETile[][] world, int width, int height) {
        world[x][y] = Tileset.WALL;
        for (Position p : getAroundPositions(width, height, false)) {
            if (p.isDeadEnd(world, width, height)) {
                p.carveDeadEnd(world, width, height);
            }
        }
    }

    public boolean isDeadEnd(TETile[][] world, int width, int height) {
        if (world[x][y] != Tileset.FLOOR) {
            return false;
        }
        int num = 0;
        for (Position p : getAroundPositions(width, height, false)) {
            if (p.isWall(world)) {
                num++;
            }
        }
        if (num == 3) {
            return true;
        }
        return false;
    }

    private List<Position> getAroundPositions(int width, int height, boolean b) {
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
        if (b == true) {
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

    private boolean isWall(TETile[][] world) {
        return world[x][y] == Tileset.WALL;
    }

}

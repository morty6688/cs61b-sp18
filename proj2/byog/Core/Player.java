package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Player {
    private static Position pos;

    public static Position getPos() {
        return pos;
    }

    public static void setPos(Position pos) {
        Player.pos = pos;
    }

    public static void walkLeft(TETile[][] world) {
        Position newPos = new Position(pos.getX() - 1, pos.getY());
        walk(world, newPos);
    }

    public static void walkRight(TETile[][] world) {
        Position newPos = new Position(pos.getX() + 1, pos.getY());
        walk(world, newPos);
    }

    public static void walkUp(TETile[][] world) {
        Position newPos = new Position(pos.getX(), pos.getY() + 1);
        walk(world, newPos);
    }

    public static void walkDown(TETile[][] world) {
        Position newPos = new Position(pos.getX(), pos.getY() - 1);
        walk(world, newPos);
    }

    private static void walk(TETile[][] world, Position newPos) {
        if (newPos.isTile(world, Tileset.FLOOR)) {
            pos.drawTile(world, Tileset.FLOOR);
            newPos.drawTile(world, Tileset.PLAYER);
            pos = newPos;
        }
    }

}

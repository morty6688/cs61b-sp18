package byog.Core;

import java.util.ArrayList;
import java.util.List;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Room {
    private Position bottomLeft;
    private Position upRight;

    private static int roomMaxNum = 25;
    private static int roomMaxWidth = 8;
    private static int roomMaxHeight = 6;

    public Room() {
    }

    public Room(Position bottomLeft, Position upRight) {
        this.bottomLeft = bottomLeft;
        this.upRight = upRight;
    }

    public boolean isOverlapped(Room other) {
        Position firstCentre = getCentralPos();
        Position secondCentre = other.getCentralPos();
        // getWidth() / 2 + other.getWidth() / 2 + 2
        // "+ 2" to avoid two rooms being too near
        boolean isXOver = Math.abs(firstCentre.x - secondCentre.x) <= getWidth() / 2 + other.getWidth() / 2 + 2;
        boolean isYOver = Math.abs(firstCentre.y - secondCentre.y) <= getHeight() / 2 + other.getHeight() / 2 + 2;
        return isXOver && isYOver;
    }

    public boolean isOverlapped(List<Room> rooms) {
        if (rooms.isEmpty()) {
            return false;
        }
        for (Room room : rooms) {
            if (this.isOverlapped(room)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLegal(Room r) {
        // not same x
        boolean isXLegal = r.bottomLeft.x != r.upRight.x;
        // not same y
        boolean isYLegal = r.bottomLeft.y != r.upRight.y;
        // assure legal width
        boolean isWidthLegal = r.getWidth() <= Room.roomMaxWidth;
        // assure legal height
        boolean isHeightLegal = r.getHeight() <= Room.roomMaxHeight;

        return isXLegal && isYLegal && isWidthLegal && isHeightLegal;
    }

    public void drawRoom(TETile[][] world, TETile t) {
        for (int i = bottomLeft.x; i <= upRight.x; i++) {
            for (int j = bottomLeft.y; j <= upRight.y; j++) {
                world[i][j] = t;
            }
        }
    }

    public List<Connector> findConnectors(TETile[][] world, int width, int height) {
        List<Connector> res = new ArrayList<>();
        for (int i = bottomLeft.x; i <= upRight.x; i++) {
            Position p1 = new Position(i, bottomLeft.y);
            Connector.addConnectableDirection(res, world, Tileset.FLOOR, Direction.DOWN, p1, width, height);
            Position p2 = new Position(i, upRight.y);
            Connector.addConnectableDirection(res, world, Tileset.FLOOR, Direction.UP, p2, width, height);
        }
        for (int j = bottomLeft.y; j <= upRight.y; j++) {
            Position p1 = new Position(bottomLeft.x, j);
            Connector.addConnectableDirection(res, world, Tileset.FLOOR, Direction.LEFT, p1, width, height);
            Position p2 = new Position(upRight.x, j);
            Connector.addConnectableDirection(res, world, Tileset.FLOOR, Direction.RIGHT, p2, width, height);
        }
        return res;
    }

    public int getWidth() {
        return upRight.x - bottomLeft.x;
    }

    public int getHeight() {
        return upRight.y - bottomLeft.y;
    }

    public Position getCentralPos() {
        return new Position((bottomLeft.x + upRight.x) / 2, (bottomLeft.y + upRight.y) / 2);
    }

    public Position getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(Position bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public Position getUpRight() {
        return upRight;
    }

    public void setUpRight(Position upRight) {
        this.upRight = upRight;
    }

    public static int getRoomMaxNum() {
        return roomMaxNum;
    }

    public static void setRoomMaxNum(int roomMaxNum) {
        Room.roomMaxNum = roomMaxNum;
    }

    public static int getRoomMaxWidth() {
        return roomMaxWidth;
    }

    public static void setRoomMaxWidth(int roomMaxWidth) {
        Room.roomMaxWidth = roomMaxWidth;
    }

    public static int getRoomMaxHeight() {
        return roomMaxHeight;
    }

    public static void setRoomMaxHeight(int roomMaxHeight) {
        Room.roomMaxHeight = roomMaxHeight;
    }
}

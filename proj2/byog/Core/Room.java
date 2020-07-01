package byog.Core;

import java.util.ArrayList;
import java.util.List;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Room {
    private Position bottomLeft;
    private Position upRight;

    private static int roomMaxNum = 20;
    private static int roomMaxWidth = 8;
    private static int roomMaxHeight = 6;

    public Room(Position bottomLeft, Position upRight) {
        this.bottomLeft = bottomLeft;
        this.upRight = upRight;
    }

    public boolean isOverlapped(Room other) {
        Position firstCentre = getCentralPos();
        Position secondCentre = other.getCentralPos();
        /*
         * the right "+ 2" to avoid two rooms being too near
         */
        int xCentreDiff = Math.abs(firstCentre.getX() - secondCentre.getX());
        boolean isXOver = xCentreDiff <= getWidth() / 2 + other.getWidth() / 2 + 2;
        int yCentreDiff = Math.abs(firstCentre.getY() - secondCentre.getY());
        boolean isYOver = yCentreDiff <= getHeight() / 2 + other.getHeight() / 2 + 2;
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
        boolean isXLegal = r.bottomLeft.getX() != r.upRight.getX();
        // not same y
        boolean isYLegal = r.bottomLeft.getY() != r.upRight.getY();
        // assure legal width
        boolean isWidthLegal = r.getWidth() < Room.roomMaxWidth;
        // assure legal height
        boolean isHeightLegal = r.getHeight() < Room.roomMaxHeight;

        return isXLegal && isYLegal && isWidthLegal && isHeightLegal;
    }

    public void drawRoom(TETile[][] world, TETile t) {
        for (int i = bottomLeft.getX(); i <= upRight.getX(); i++) {
            for (int j = bottomLeft.getY(); j <= upRight.getY(); j++) {
                world[i][j] = t;
            }
        }
    }

    public List<Connector> findConnectors(TETile[][] world, int width, int height) {
        List<Connector> res = new ArrayList<>();
        for (int i = bottomLeft.getX(); i <= upRight.getX(); i++) {
            Position p1 = new Position(i, bottomLeft.getY());
            Connector.addConnectableDirection(res, world, Tileset.FLOOR, Direction.DOWN, p1, width,
                    height);
            Position p2 = new Position(i, upRight.getY());
            Connector.addConnectableDirection(res, world, Tileset.FLOOR, Direction.UP, p2, width,
                    height);
        }
        for (int j = bottomLeft.getY(); j <= upRight.getY(); j++) {
            Position p1 = new Position(bottomLeft.getX(), j);
            Connector.addConnectableDirection(res, world, Tileset.FLOOR, Direction.LEFT, p1, width,
                    height);
            Position p2 = new Position(upRight.getX(), j);
            Connector.addConnectableDirection(res, world, Tileset.FLOOR, Direction.RIGHT, p2, width,
                    height);
        }
        return res;
    }

    public int getWidth() {
        return upRight.getX() - bottomLeft.getX();
    }

    public int getHeight() {
        return upRight.getY() - bottomLeft.getY();
    }

    public Position getCentralPos() {
        return new Position((bottomLeft.getX() + upRight.getX()) / 2,
                (bottomLeft.getY() + upRight.getY()) / 2);
    }

    public static int getRoomMaxNum() {
        return roomMaxNum;
    }

    public static void setRoomMaxNum(int roomMaxNum) {
        Room.roomMaxNum = roomMaxNum;
    }
}

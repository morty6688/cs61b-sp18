package byog.Core;

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
        boolean isXLegal = r.getBottomLeft().x != r.getUpRight().x;
        // not same y
        boolean isYLegal = r.getBottomLeft().y != r.getUpRight().y;
        // assure legal width
        boolean isWidthLegal = r.getWidth() <= Room.roomMaxWidth;
        // assure legal height
        boolean isHeightLegal = r.getHeight() <= Room.roomMaxHeight;

        return isXLegal && isYLegal && isWidthLegal && isHeightLegal;
    }

    public void drawRoom(TETile[][] world) {
        for (int i = bottomLeft.x; i <= upRight.x; i++) {
            for (int j = bottomLeft.y; j <= upRight.y; j++) {
                world[i][j] = Tileset.ROOMFLOOR;
            }
        }
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

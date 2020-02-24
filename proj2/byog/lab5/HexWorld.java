package byog.lab5;

import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    /**
     * Adds a hexagon to the world.
     * 
     * @param world the world to draw on
     * @param p     the bottom left coordinate of the hexagon
     * @param s     the size of the hexagon
     * @param t     the tile to draw
     */
    public static void addHexagon(TETile[][] world, TETile t, Position p, int s) {
        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }

        for (int i = 0; i < maxRow(s); i++) {
            for (int j = 0; j < maxHeight(s); j++) {
                if (hasTile(i, j, s)) {
                    world[p.x + i][p.y + j] = TETile.colorVariant(t, 64, 64, 64, new Random());
                }
            }
        }
    }

    private static int maxHeight(int s) {
        return 2 * s;
    }

    private static int maxRow(int s) {
        return 3 * s - 2;
    }

    private static boolean hasTile(int i, int j, int s) {
        // lower left corner
        if (j < s && i + j < s - 1) {
            return false;
        }
        // upper left corner
        if (j >= s && j - i > s) {
            return false;
        }
        // lower right corner
        if (j < s && i - j > maxHeight(s) - 2) {
            return false;
        }
        // upper right corner
        if (j >= s && i + j > 4 * s - 3) {
            return false;
        }
        return true;
    }

    @Test
    public void testHasTile() {
        assertTrue(hasTile(0, 2, 3));
        assertTrue(hasTile(4, 0, 3));
        assertTrue(hasTile(1, 4, 3));
        assertTrue(hasTile(5, 4, 3));
        assertTrue(hasTile(3, 3, 3));
        assertFalse(hasTile(0, 1, 3));
        assertFalse(hasTile(5, 0, 3));
        assertFalse(hasTile(1, 5, 3));
        assertFalse(hasTile(6, 4, 3));

        assertTrue(hasTile(0, 1, 2));
        assertTrue(hasTile(0, 2, 2));
        assertTrue(hasTile(2, 1, 2));
        assertTrue(hasTile(3, 2, 2));
        assertFalse(hasTile(0, 0, 2));
        assertFalse(hasTile(3, 0, 2));
        assertFalse(hasTile(0, 3, 2));
        assertFalse(hasTile(3, 3, 2));
    }

    @Test
    public void testAddHexagon() {
        int width = 60;
        int height = 40;
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        TETile[][] world = new TETile[width][height];

        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        HexWorld.addHexagon(world, Tileset.TREE, new Position(), 5);

        ter.renderFrame(world);
    }

    public static void main(String[] args) {
        int width = 30;
        int height = 30;
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        TETile[][] world = new TETile[width][height];

        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        int s = 3;
        Position p1 = new Position(0, 2 * s);
        Position p2 = bottomRightNeighbor(p1, s);
        Position p3 = bottomRightNeighbor(p2, s);
        Position p4 = topRightNeighbor(p3, s);
        Position p5 = topRightNeighbor(p4, s);
        drawRandomVerticalHexes(world, 3, p1, s);
        drawRandomVerticalHexes(world, 4, p2, s);
        drawRandomVerticalHexes(world, 5, p3, s);
        drawRandomVerticalHexes(world, 4, p4, s);
        drawRandomVerticalHexes(world, 3, p5, s);

        ter.renderFrame(world);
    }

    private static Position bottomRightNeighbor(Position p1, int s) {
        return new Position(p1.x + 2 * s - 1, p1.y - s);
    }

    private static Position topRightNeighbor(Position p1, int s) {
        return new Position(p1.x + 2 * s - 1, p1.y + s);

    }

    private static void drawRandomVerticalHexes(TETile[][] world, int num, Position p, int s) {
        TETile[] tiles = new TETile[] { Tileset.FLOWER, Tileset.GRASS, Tileset.MOUNTAIN, Tileset.SAND, Tileset.TREE,
                Tileset.WATER };
        Random r = new Random();
        for (int i = 0; i < num; i++) {
            HexWorld.addHexagon(world, tiles[r.nextInt(tiles.length)], p, s);
            p.y += 2 * s;
        }
    }

}

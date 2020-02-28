package byog.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 81;
    public static final int HEIGHT = 31;

    /**
     * Method used for playing a fresh game. The game should start from the main
     * menu.
     */
    public void playWithKeyboard() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        initializeWorld(world);

        Random r = new Random();

        Stack<Position> stack = new Stack<>();
        Position startPoint = decideStartPoint(r);
        world[startPoint.x][startPoint.y] = Tileset.START;
        stack.push(startPoint);

        generateMaze(world, r, stack);

        ter.renderFrame(world);
    }

    private void generateMaze(TETile[][] world, Random r, Stack<Position> stack) {
        while (!stack.isEmpty()) {
            Position existed = stack.peek();
            Position p = nextPos(r, existed, world);
            if (p == null) {
                stack.pop();
                continue;
            }
            // isVisited[p.x][p.y] = true;
            world[p.x][p.y] = Tileset.DEVELOPEDFLOOR;
            if (p.x > existed.x) {
                world[existed.x + 1][existed.y] = Tileset.DEVELOPEDFLOOR;
            } else if (p.x < existed.x) {
                world[existed.x - 1][existed.y] = Tileset.DEVELOPEDFLOOR;
            } else if (p.y > existed.y) {
                world[existed.x][existed.y + 1] = Tileset.DEVELOPEDFLOOR;
            } else if (p.y < existed.y) {
                world[existed.x][existed.y - 1] = Tileset.DEVELOPEDFLOOR;
            }
            stack.push(p);
        }
    }

    private void initializeWorld(TETile[][] world) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.WALL;
            }
        }

        for (int x = 1; x < WIDTH; x += 2) {
            for (int y = 1; y < HEIGHT; y += 2) {
                world[x][y] = Tileset.UNDEVFLOOR;
            }
        }
    }

    private Position nextPos(Random r, Position p, TETile[][] world) {
        List<Position> possiblePos = new ArrayList<>();
        if (p.x + 2 >= 0 && p.x + 2 < WIDTH && world[p.x + 2][p.y] == Tileset.UNDEVFLOOR) {
            possiblePos.add(new Position(p.x + 2, p.y));
        }
        if (p.x - 2 >= 0 && p.x - 2 < WIDTH && world[p.x - 2][p.y] == Tileset.UNDEVFLOOR) {
            possiblePos.add(new Position(p.x - 2, p.y));
        }
        if (p.y + 2 >= 0 && p.y + 2 < HEIGHT && world[p.x][p.y + 2] == Tileset.UNDEVFLOOR) {
            possiblePos.add(new Position(p.x, p.y + 2));
        }
        if (p.y - 2 >= 0 && p.y - 2 < HEIGHT && world[p.x][p.y - 2] == Tileset.UNDEVFLOOR) {
            possiblePos.add(new Position(p.x, p.y - 2));
        }
        if (possiblePos.isEmpty()) {
            return null;
        }
        int selector = RandomUtils.uniform(r, 0, possiblePos.size());
        return possiblePos.get(selector);
    }

    private Position decideStartPoint(Random r) {
        Position p = new Position();
        int selector = RandomUtils.uniform(r, 0, 4);
        switch (selector) {
            case 0:
                p.x = 1;
                p.y = decideXOrY(r, 1, HEIGHT - 1);
                break;
            case 1:
                p.y = 1;
                p.x = decideXOrY(r, 1, WIDTH - 1);
                break;
            case 2:
                p.x = WIDTH - 2;
                p.y = decideXOrY(r, 1, HEIGHT - 1);
                break;
            case 3:
                p.y = HEIGHT - 2;
                p.x = decideXOrY(r, 1, WIDTH - 1);
                break;
        }
        return p;
    }

    private int decideXOrY(Random r, int start, int end) {
        int x = RandomUtils.uniform(r, start, end);
        if (x % 2 == 0) {
            if (RandomUtils.bernoulli(r)) {
                x++;
            } else {
                x--;
            }
        }
        return x;
    }

    /**
     * Method used for autograding and testing the game code. The input string will
     * be a series of characters (for example, "n123sswwdasdassadwas", "n123sss:q",
     * "lwww". The game should behave exactly as if the user typed these characters
     * into the game after playing playWithKeyboard. If the string ends in ":q", the
     * same world should be returned as if the string did not end with q. For
     * example "n123sss" and "n123sss:q" should return the same world. However, the
     * behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string
     * "l", we'd expect to get the exact same world back again, since this
     * corresponds to loading the saved game.
     * 
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }
}

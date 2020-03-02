package byog.Core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
        long seed = 0;
        input = toLower(input);
        TETile[][] finalWorldFrame = null;
        char firstChar = input.charAt(0);
        if (firstChar == 'n') {
            seed = getSeed(input);
            finalWorldFrame = generateWorld(seed);
        } else if (firstChar == 'l') {
            return loadGame();
        }
        if (input.substring(input.length() - 2).equals(":q")) {
            saveGame(seed);
        }
        return finalWorldFrame;
    }

    /**
     * Method used for playing a fresh game. The game should start from the main
     * menu.
     */
    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);

        long seed = new Random().nextLong();
        TETile[][] finalWorldFrame = generateWorld(seed);

        ter.renderFrame(finalWorldFrame);
    }

    private void saveGame(long seed) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("savefile.txt"));
            out.write(String.valueOf(seed));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TETile[][] loadGame() {
        TETile[][] finalWorldFrame = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader("savefile.txt"));
            long seed = Long.valueOf(in.readLine());
            finalWorldFrame = generateWorld(seed);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalWorldFrame;
    }

    private long getSeed(String input) {
        StringBuilder seedNum = new StringBuilder();
        for (int i = 1; input.charAt(i) != 's'; i++) {
            seedNum.append(input.charAt(i));
        }
        return Long.valueOf(seedNum.toString());
    }

    private String toLower(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (Character.isUpperCase(ch)) {
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private TETile[][] generateWorld(long seed) {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        initializeWorld(world);

        Random r = new Random(seed);

        List<Room> rooms = generateRooms(world, r, 10);
        generateHalls(world, r);
        generateConnectors(world, r, rooms);
        if (!rooms.isEmpty()) {
            carveDeadEnds(world);
        }
        carveExtraWalls(world);
        addADoor(world, r);
        return world;
    }

    private void addADoor(TETile[][] world, Random r) {
        List<Position> edges = new ArrayList<>();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Position p = new Position(i, j);
                if (p.isEdge(world, WIDTH, HEIGHT)) {
                    edges.add(p);
                }
            }
        }
        int selector = RandomUtils.uniform(r, 0, edges.size());
        edges.get(selector).drawTile(world, Tileset.LOCKED_DOOR);
    }

    private void carveExtraWalls(TETile[][] world) {
        List<Position> solidWalls = new ArrayList<>();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Position p = new Position(i, j);
                if (p.isSolidWall(world, WIDTH, HEIGHT)) {
                    solidWalls.add(p);
                }
            }
        }

        for (Position p : solidWalls) {
            p.drawTile(world, Tileset.NOTHING);
        }
    }

    private void carveDeadEnds(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Position p = new Position(i, j);
                if (p.isDeadEnd(world, WIDTH, HEIGHT)) {
                    p.carveDeadEnd(world, WIDTH, HEIGHT);
                }
            }
        }
    }

    private void generateConnectors(TETile[][] world, Random r, List<Room> rooms) {
        for (Room room : rooms) {
            List<Connector> connectors = room.findConnectors(world, WIDTH, HEIGHT);
            if (connectors.size() == 0) {
                continue;
            }
            int numOfSelection = RandomUtils.uniform(r, 1, 4);
            for (int i = 0; i < numOfSelection; i++) {
                int selector = RandomUtils.uniform(r, 0, connectors.size());
                connectors.get(selector).connect(world, Tileset.FLOOR);
            }
            // int selector = RandomUtils.uniform(r, 0, connectors.size());
            // connectors.get(selector).connect(world, Tileset.FLOOR);
        }

        for (Room room : rooms) {
            room.drawRoom(world, Tileset.FLOOR);
        }
    }

    private List<Room> generateRooms(TETile[][] world, Random r, int roomNum) {
        Room.setRoomMaxNum(roomNum);
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < Room.getRoomMaxNum(); ) {
            Room newRoom;
            do {
                Position p1 = new Position(decideXOrY(r, 1, WIDTH - 3), decideXOrY(r, 1,
                        HEIGHT - 3));
                Position p2 = new Position(decideXOrY(r, p1.getX() + 1, WIDTH - 1),
                        decideXOrY(r, p1.getY() + 1, HEIGHT - 1));
                newRoom = new Room(p1, p2);
            } while (!Room.isLegal(newRoom));
            if (!newRoom.isOverlapped(rooms)) {
                rooms.add(newRoom);
                i++;
                newRoom.drawRoom(world, Tileset.ROOMFLOOR);
            }
        }
        return rooms;
    }

    private void generateHalls(TETile[][] world, Random r) {
        // dfs
        Stack<Position> stack = new Stack<>();
        Position startPoint = decideStartPoint(r, world);
        startPoint.drawTile(world, Tileset.FLOOR);
        stack.push(startPoint);
        while (!stack.isEmpty()) {
            Position existed = stack.peek();
            Connector conn = nextConnector(r, existed, world);
            if (conn == null) {
                stack.pop();
                continue;
            }
            conn.getGoalPos().drawTile(world, Tileset.FLOOR);
            conn.connect(world, Tileset.FLOOR);
            stack.push(conn.getGoalPos());
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

    private Connector nextConnector(Random r, Position p, TETile[][] world) {
        List<Connector> possibleConnectors = new ArrayList<>();
        for (Direction d : Direction.values()) {
            Connector.addConnectableDirection(possibleConnectors, world, Tileset.UNDEVFLOOR, d,
                    p, WIDTH, HEIGHT);
        }
        if (possibleConnectors.isEmpty()) {
            return null;
        }
        int selector = RandomUtils.uniform(r, 0, possibleConnectors.size());
        return possibleConnectors.get(selector);
    }

    private Position decideStartPoint(Random r, TETile[][] world) {
        Position p = new Position();
        int selector = RandomUtils.uniform(r, 0, 4);
        switch (selector) {
            case 0:
                p.setX(1);
                do {
                    p.setY(decideXOrY(r, 1, HEIGHT - 1));
                } while (p.isTile(world, Tileset.ROOMFLOOR));
                break;
            case 1:
                p.setY(1);
                do {
                    p.setX(decideXOrY(r, 1, WIDTH - 1));
                } while (p.isTile(world, Tileset.ROOMFLOOR));
                break;
            case 2:
                p.setX(WIDTH - 2);
                do {
                    p.setY(decideXOrY(r, 1, HEIGHT - 1));
                } while (p.isTile(world, Tileset.ROOMFLOOR));
                break;
            default:
                p.setY(HEIGHT - 2);
                do {
                    p.setX(decideXOrY(r, 1, WIDTH - 1));
                } while (p.isTile(world, Tileset.ROOMFLOOR));
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

}

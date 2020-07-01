package byog.Core;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

public class Game {
    TERenderer ter = new TERenderer();
    /*
     * Feel free to change the width and height. (Better be odd number.)
     */
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
        input = toLower(input);
        TETile[][] finalWorldFrame = null;
        char firstChar = input.charAt(0);
        if (firstChar == 'n') {
            finalWorldFrame = newGame(input);
        } else if (firstChar == 'l') {
            finalWorldFrame = loadGame(input);
        } else if (firstChar == 'q') {
            System.exit(0);
        }
        return finalWorldFrame;
    }

    /**
     * Method used for playing a fresh game. The game should start from the main
     * menu.
     */
    public void playWithKeyboard() {
        drawStartUI();
        char firstChar = getFirstChar();
        if (firstChar == 'n') {
            newGame();
        } else if (firstChar == 'l') {
            loadGame();
        } else if (firstChar == 'q') {
            System.exit(0);
        }
    }

    /**
     * New game with keyboard.
     */
    private void newGame() {
        long seed = getSeed();
        ter.initialize(WIDTH, HEIGHT + 1);
        TETile[][] world = generateWorld(seed);
        ter.renderFrame(world);
        play(world);
    }

    /**
     * New game with String.
     */
    private TETile[][] newGame(String input) {
        TETile[][] finalWorldFrame;
        int indexS = input.indexOf('s');
        long seed = convertSeed(input.substring(1, indexS));
        finalWorldFrame = generateWorld(seed);

        play(finalWorldFrame, input.substring(indexS + 1));
        return finalWorldFrame;
    }

    /**
     * Load game with String.
     */
    private TETile[][] loadGame(String input) {
        TETile[][] finalWorldFrame;
        finalWorldFrame = getSavedGame();
        play(finalWorldFrame, input.substring(1));
        return finalWorldFrame;
    }

    /**
     * Load game with keyboard.
     */
    private void loadGame() {
        ter.initialize(WIDTH, HEIGHT + 1);
        TETile[][] world = getSavedGame();
        ter.renderFrame(world);
        play(world);
    }

    /**
     * Play game with String.
     */
    private void play(TETile[][] world, String playString) {
        for (int i = 0; i < playString.length(); i++) {
            switch (playString.charAt(i)) {
                case 'w':
                    Player.walkUp(world);
                    break;
                case 'a':
                    Player.walkLeft(world);
                    break;
                case 's':
                    Player.walkDown(world);
                    break;
                case 'd':
                    Player.walkRight(world);
                    break;
                case ':':
                    if (i + 1 < playString.length() && playString.charAt(i + 1) == 'q') {
                        saveGame(world);
                        return;
                    }
                    break;
                default:
            }
        }
    }

    /**
     * Play game with keyboard.
     */
    private void play(TETile[][] world) {
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char c = Character.toLowerCase(StdDraw.nextKeyTyped());
            switch (c) {
                case 'w':
                    Player.walkUp(world);
                    ter.renderFrame(world);
                    break;
                case 'a':
                    Player.walkLeft(world);
                    ter.renderFrame(world);
                    break;
                case 's':
                    Player.walkDown(world);
                    ter.renderFrame(world);
                    break;
                case 'd':
                    Player.walkRight(world);
                    ter.renderFrame(world);
                    break;
                case ':':
                    while (true) {
                        if (!StdDraw.hasNextKeyTyped()) {
                            continue;
                        }
                        if (Character.toLowerCase(StdDraw.nextKeyTyped()) == 'q') {
                            saveGame(world);
                            System.exit(0);
                        } else {
                            break;
                        }
                    }
                    break;
                default:
            }
        }
    }

    /**
     * Get seed from keyboard.
     */
    private long getSeed() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 50));
        StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "Please enter a random seed:");
        StdDraw.show();
        String seedString = "";
        while (true) {
            StdDraw.clear(Color.BLACK);
            StdDraw.setFont(new Font("Monaco", Font.PLAIN, 50));
            StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "Please enter a random seed:");

            char digit;
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            digit = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (digit != 's') {
                if (!Character.isDigit(digit)) {
                    continue;
                }
                seedString += digit;
                StdDraw.setFont(new Font("Monaco", Font.PLAIN, 30));
                StdDraw.text(WIDTH / 2, HEIGHT / 2, seedString);
                StdDraw.show();
            } else {
                break;
            }
        }
        long seed = convertSeed(seedString);
        return seed;
    }

    private char getFirstChar() {
        char c;
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            c = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (c == 'n' || c == 'l' || c == 'q') {
                break;
            }
        }
        return c;
    }

    private void saveGame(TETile[][] finalWorldFrame) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savefile.txt"));
            out.writeObject(finalWorldFrame);
            out.writeObject(Player.getPos());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TETile[][] getSavedGame() {
        TETile[][] finalWorldFrame = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("savefile.txt"));
            finalWorldFrame = (TETile[][]) in.readObject();
            Player.setPos((Position) in.readObject());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return finalWorldFrame;
    }

    private long convertSeed(String seedString) {
        return Long.valueOf(seedString.toString());
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

    // reference: https://zhuanlan.zhihu.com/p/27381213
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
        addDoorAndInitialPlayer(world, r);
        return world;
    }

    private void addDoorAndInitialPlayer(TETile[][] world, Random r) {
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
        Position selected = edges.get(selector);
        selected.drawTile(world, Tileset.LOCKED_DOOR);
        selected.drawInitialPerson(world, WIDTH, HEIGHT);
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
        for (int i = 0; i < Room.getRoomMaxNum();) {
            Room newRoom;
            do {
                Position p1 =
                        new Position(decideXOrY(r, 1, WIDTH - 3), decideXOrY(r, 1, HEIGHT - 3));
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
            Connector.addConnectableDirection(possibleConnectors, world, Tileset.UNDEVFLOOR, d, p,
                    WIDTH, HEIGHT);
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

    private void drawStartUI() {
        initializeCanvas();

        Font font = new Font("Monaco", Font.PLAIN, 60);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "CS61B: THE GAME");

        Font smallFont = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(smallFont);
        StdDraw.text(WIDTH / 2, HEIGHT / 4 + 2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 4, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 4 - 2, "Quit (Q)");

        StdDraw.show();
    }

    private void initializeCanvas() {
        StdDraw.setCanvasSize(WIDTH * 16, (HEIGHT + 1) * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT + 1);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
    }
}

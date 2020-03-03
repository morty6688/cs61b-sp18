package byog.lab6;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import edu.princeton.cs.introcs.StdDraw;

public class MemoryGame {
    private int width;
    private int height;
    private int round = 1;
    private Random rand;
    private boolean gameOver = false;
    private boolean playerTurn = false;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = { "You can do this!", "I believe in you!", "You got this!",
            "You're a star!", "Go Bears!", "Too easy for you!", "Wow, so impressive!" };

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /*
         * Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as
         * its canvas Also sets up the scale so the top left is (0,0) and the bottom
         * right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        rand = new Random(seed);
    }

    /** Generate random string of letters of length n */
    public String generateRandomString(int n, Random r) {
        if (n <= 0) {
            throw new IllegalArgumentException("argument n must be positive: " + n);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(CHARACTERS[r.nextInt(CHARACTERS.length)]);
        }
        return sb.toString();
    }

    /**
     * Take the string s and display it in the center of the screen. If game is not
     * over, display relevant game information at the top of the screen
     */
    public void drawFrame(String s) {
        int midWidth = width / 2;
        int midHeight = height / 2;

        StdDraw.clear(Color.BLACK);

        if (!gameOver) {
            Font smallFont = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(smallFont);
            StdDraw.textLeft(1, height - 1, "Round: " + round);
            StdDraw.text(midWidth, height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
            StdDraw.line(0, height - 2, width, height - 2);
        }

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(midWidth, midHeight, s);
        StdDraw.show();
    }

    /**
     * Display each character in letters, making sure to blank the screen between
     * letters
     */
    public void flashSequence(String letters) {
        for (char c : letters.toCharArray()) {
            drawFrame(String.valueOf(c));
            StdDraw.pause(1000);
            drawFrame(" ");
            StdDraw.pause(500);
        }
    }

    /** Read n letters of player input */
    public String solicitNCharsInput(int n) {
        String input = "";
        drawFrame(input);

        while (input.length() < n) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            input += StdDraw.nextKeyTyped();
            drawFrame(input);
        }
        StdDraw.pause(500);
        return input;
    }

    public void startGame() {
        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round: " + round);
            StdDraw.pause(1500);

            String randomString = generateRandomString(round, rand);
            flashSequence(randomString);

            playerTurn = true;
            String userString = solicitNCharsInput(round);
            if (userString.equals(randomString)) {
                drawFrame("Correct, well done!");
                round++;
                StdDraw.pause(1500);
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round);
            }
        }

    }

}

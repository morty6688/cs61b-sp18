package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;

/**
 * MouseGame
 */
public class MouseGame {
    private static int width;
    private static int height;

    public static void main(String[] args) {
        new MouseGame(40, 40);
        while (true) {
            drawFrame();
        }
    }

    public MouseGame(int width, int height) {
        MouseGame.width = width;
        MouseGame.height = height;
        StdDraw.setCanvasSize(width * 16, height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public static void drawFrame() {
        int midWidth = width / 2;
        int midHeight = height / 2;

        StdDraw.clear(Color.BLACK);

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(midWidth, midHeight, String.valueOf((int) StdDraw.mouseX()));
        StdDraw.text(midWidth, midHeight - 3, String.valueOf((int) StdDraw.mouseY()));
        StdDraw.show();
    }
}

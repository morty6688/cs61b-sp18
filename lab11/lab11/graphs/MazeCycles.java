package lab11.graphs;

import java.util.Random;

/**
 * @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /*
     * Inherits public fields: public int[] distTo; public int[] edgeTo; public
     * boolean[] marked;
     */
    private int[] pathTo;
    private boolean circleFound = false;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        pathTo = new int[maze.V()];
        // int s = 0;
        Random rand = new Random();
        int startX = rand.nextInt(maze.N());
        int startY = rand.nextInt(maze.N());
        int s = maze.xyTo1D(startX, startY);
        pathTo[s] = s;
        dfs(s);
        announce();
    }

    private void dfs(int v) {
        marked[v] = true;
        for (int w : maze.adj(v)) {

            if (circleFound) {
                return;
            }

            if (!marked[w]) {
                pathTo[w] = v;
                dfs(w);
            } else if (w != pathTo[v]) {
                // w is not the parent of v (circle found)
                pathTo[w] = v;

                int cur = v;
                edgeTo[cur] = pathTo[cur];
                while (cur != w) {
                    cur = pathTo[cur];
                    edgeTo[cur] = pathTo[cur];
                }
                circleFound = true;
                return;
            }
        }
    }
}

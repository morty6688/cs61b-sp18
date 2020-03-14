package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /*
     * Inherits public fields: public int[] distTo(display number); public int[]
     * edgeTo(display edge); public boolean[] marked(display all);
     */
    private int s;
    private int t;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        Queue<Integer> q = new ArrayDeque<>();
        q.add(s);
        marked[s] = true;
        while (!q.isEmpty()) {
            int v = q.poll();
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    announce();
                    if (w == t) {
                        return;
                    } else {
                        q.add(w);
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        bfs();
    }
}

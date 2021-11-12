package lab11.graphs;

import edu.princeton.cs.algs4.Stack;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Stack<Integer> stack = new Stack<Integer>();
    private boolean cycle = false;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        dfs(0, -1);
    }

    // Helper methods go here
    private boolean allMarked() {
        for (boolean m : marked) {
            if (!m)
                return false;
        }
        return true;
    }
    private boolean dfs(int v, int p) {
        if (allMarked() || cycle)
            return false;
        marked[v] = true;
        announce();
        boolean foundCycle = false;
        for (int w : maze.adj(v)) {
            if (marked[w] && w != p){
                edgeTo[w] = v;
                announce();
                cycle = true;
                return true;
            } else if(!marked[w]) {
                foundCycle |= dfs(w, v);
            }
        }
        return foundCycle;
    }
}


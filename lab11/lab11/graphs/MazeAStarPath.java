package lab11.graphs;

import java.lang.Math;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private PriorityQueue<PQNode> pq = new PriorityQueue<PQNode>(new PQNodeComparator());

    private static class PQNode {
        public int v;
        public int prio;
        public PQNode(int vertex, int priority) {
            v = vertex;
            prio = priority;
        }
    }

    private static class PQNodeComparator implements Comparator<PQNode> {
        @Override
        public int compare(PQNode o1, PQNode o2) {
            return o1.prio - o2.prio;
        }
    }

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int sourceX = maze.toX(v);
        int sourceY = maze.toY(v);
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);
        return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        pq.add(new PQNode(s, (distTo[s] + h(s))));
        while (!pq.isEmpty()) {
            PQNode v = pq.remove();
            if (v.v == t) {
                marked[v.v] = true;
                announce();
                return;
            }
            for (int w : maze.adj(v.v)) {
                if (!marked[w]) {
                    edgeTo[w] = v.v;
                    distTo[w] = distTo[v.v] + 1;
                    announce();
                    pq.add(new PQNode(w, (distTo[w] + h(w))));
                }
            }
            marked[v.v] = true;
            announce();
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}


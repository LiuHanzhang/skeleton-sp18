package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

public class Solver {
    private SearchNode solutionSearchNode = null; // The SearchNode representing solution
    public int insertCount = 0; // Number of total things ever enqueued in MinPQ

    private static class SearchNode {
        public WorldState worldstate;
        public int nmoves; // #moves made to reach this world state from the initial state.
        public SearchNode parent; // A reference to the previous search node.

        public SearchNode(WorldState ws, int nm, SearchNode p) {
            worldstate = ws;
            nmoves = nm;
            parent = p;
        }
    }

    private static class SearchNodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            return (o1.nmoves + o1.worldstate.estimatedDistanceToGoal()) -
                    (o2.nmoves + o2.worldstate.estimatedDistanceToGoal());
        }
    }

    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>(new SearchNodeComparator());
        pq.insert(new SearchNode(initial, 0, null));
        ++insertCount;

        while (!pq.isEmpty()) {
            SearchNode x = pq.delMin();
            if (x.worldstate.isGoal()) {
                solutionSearchNode = x;
                break;
            } else {
                for (WorldState ws : x.worldstate.neighbors()) {
                    if (x.parent == null || !ws.equals(x.parent.worldstate)) {
                        pq.insert(new SearchNode(ws, x.nmoves + 1, x));
                        ++insertCount;
                    }
                }
            }
        }
    }
    public int moves() { return solutionSearchNode.nmoves; }

    public Iterable<WorldState> solution() {
        SearchNode node = new SearchNode(solutionSearchNode.worldstate, solutionSearchNode.nmoves,
                solutionSearchNode.parent);
        // TRAP: Cannot use Stack, because Java Stack is implemented by Vector,
        // whose iterator is FIFO, not LIFO
        Deque<WorldState> stack = new ArrayDeque<WorldState>();
        while (node != null) {
            stack.addFirst(node.worldstate);
            node = node.parent;
        }
        return stack;
    }
}

package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;
import java.util.Arrays;

public class Board implements WorldState {
    private final int N;
    private final int [][] tiles;
    private int estimatedDistanceToGoalCache = -1;

    /** Constructs a board from an N-by-N array of tiles where
     *  tiles[i][j] = tile at row i, column j */
    public Board(int [][] tiles) {
        N = tiles.length;
        // TRAP: If we make this.tiles = tiles instead fo making a copy
        // terrible bugs happens to neighbors()
        // You can see in neighbors(): int[][] ili1li1 = new int[hug][hug];
        // i.e., one tiles array is responsible for multiple Board's construction
        // https://sp18.datastructur.es/materials/hw/hw4/hw4#:~:text=The%20neighbors()%20method%20provided%20doesn%E2%80%99t%20work.%20It%20looks%20like%20it%20only%20returns%20the%20initial%20board.
        // https://sp18.datastructur.es/materials/hw/hw4/hw4#:~:text=How%20do%20I%20ensure%20my%20Board%20class%20immutable%3F
        // NOTE: important lesson: always make a copy in this situation,
        // otherwise outsiders can change this private array with its undeleted reference to it.
        this.tiles = new int [N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, N);
        }
    }

    /** Returns value of tile at row i, column j (or 0 if blank) */
    public int tileAt(int i, int j) {
        if (0 <= i && i < N && 0 <= j && j < N) {
            return tiles[i][j];
        } else
            throw new IndexOutOfBoundsException("Invalid index");
    }

    /** Returns the board size N */
    public int size() { return N; }

    /** Returns the neighbors of the current board
     *  Source: http://joshh.ug/neighbors.html */
    @Override
    public Iterable<WorldState> neighbors() {
        final int BLANK = 0;
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int distance = 0;
        for (int row = 0; row < N; ++row)
            for (int col = 0; col < N; ++col) {
                if (tileAt(row, col) != 0 && (tileAt(row, col) != col * 3 + row + 1))
                    ++distance;
            }
        return distance;
    }

    /** A helper function that calculates the sum of the
     * vertical and horizontal distance from a tile to its goal position */
    private int distanceOff(int num, int N, int row, int col) {
        int goalRow = (num - 1) / N;
        int goalCol = (num - 1) % N;
        return Math.abs(row - goalRow) + Math.abs(goalCol - col);
    }

    public int manhattan() {
        int distance = 0;
        for (int row = 0; row < N; ++row)
            for (int col = 0; col < N; ++col) {
                if (tileAt(row, col) != 0)
                    distance += distanceOff(tileAt(row, col), N, row, col);
            }
        return distance;
    }

    /** Estimated distance to goal. This method should simply return
     * the results of manhattan() when submitted to Gradescope
     * TODO: Why is this a bottleneck? */
    @Override
    public int estimatedDistanceToGoal() {
        if (estimatedDistanceToGoalCache < 0)
            estimatedDistanceToGoalCache = manhattan();
        return estimatedDistanceToGoalCache;
    }

    /** Returns true if this board's tile values are the same
     *  position as y's */
    @Override
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y == this)
            return true;
        if (y.getClass() != this.getClass())
            return false;
        Board other = (Board)y;
        if (other.size() != this.size())
            return false;
        for (int row = 0; row < N; ++row)
            for (int col = 0; col < N; ++col) {
                if (other.tileAt(row, col) != this.tileAt(row, col))
                    return false;
            }
        return true;
    }

    /** https://www.coder.work/article/2842514 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + N;
        for (int i = 0; i < N; ++i)
            result = prime * result + Arrays.hashCode(tiles[i]);
        return result;
    }

    /** Returns the string representation of the board. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}

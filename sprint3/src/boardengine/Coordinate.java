package boardengine;

/**
 * Represents a coordinate on the board using row and column values.
 * Used to identify tile positions and calculate adjacent tiles.
 */
public class Coordinate {

    /**
     * The row index of the coordinate (0-based).
     */
    public final int row;

    /**
     * The column index of the coordinate (0-based).
     */
    public final int col;

    /**
     * Creates a new coordinate with the given row and column.
     *
     * @param x_pos the row index
     * @param y_pos the column index
     */
    public Coordinate(int x_pos, int y_pos) {
        row = x_pos;
        col = y_pos;
    }

    /**
     * Returns all 8 tiles surrounding this coordinate (diagonals included).
     *
     * @return an array of all adjacent coordinates
     */
    public Coordinate[] getAjacencies() {
        Coordinate[] output = new Coordinate[8];
        int i = 0;
        for (int row_adj = row - 1; row_adj <= row + 1; row_adj++) {
            for (int col_adj = col - 1; col_adj <= col + 1; col_adj++) {
                if (row_adj == row && col_adj == col) continue; // skip self
                output[i] = new Coordinate(row_adj, col_adj);
                i++;
            }
        }
        return output;
    }

    /**
     * Returns the 4 directly adjacent tiles (up, down, left, right).
     *
     * @return an array of orthogonally adjacent coordinates
     */
    public Coordinate[] getOrthogonalAdjacencies() {
        Coordinate[] output = new Coordinate[4];
        output[0] = new Coordinate(row - 1, col); // up
        output[1] = new Coordinate(row, col - 1); // left
        output[2] = new Coordinate(row + 1, col); // down
        output[3] = new Coordinate(row, col + 1); // right
        return output;
    }

    /**
     * Returns a human-readable string showing row and column (1-based).
     *
     * @return a string in the format "Row: X Column: Y"
     */
    @Override
    public String toString() {
        int displayRow = row + 1;
        int displayCol = col + 1;
        return "Row: " + displayRow + " Column: " + displayCol;
    }
}

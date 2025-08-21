package boardengine;

import pieces.Piece;
import pieces.Worker;
import pieces.colorablePiece;

import java.io.File;
import java.util.Scanner;
/**
 * Represents the game board, which holds the spaces and handles logic
 * for movement, building, and board state.
 */
public class Board {
    // Private
    private Space[][] spaces;
    private final int padding = 1;
    private String boardName;
    private Dimensions dims;


    /**
     * Creates a board with custom dimensions, used mostly for GUI setups.
     *
     * @param playableWidth  number of playable rows
     * @param playableHeight number of playable columns
     */
    public Board(int playableWidth, int playableHeight){
        // By Omila for GUI board printing
        // Can be changed to allow for rectangle boards
        this.dims = new Dimensions(playableWidth, playableHeight);
        // Creates new spaces based on the width and height inputted
        // +2 added for Ocean border
        spaces = new Space[dims.row + 2][dims.col + 2];

        initOceanPadding();
        initPlayableGround();
        initPerimeter();
    }

    /**
     * Gets the space at the given x and y coordinates.
     * If out of bounds, returns a dummy Ocean tile instead.
     *
     * @param x the row index
     * @param y the column index
     * @return the space at the specified location
     */
    public Space getSpace(int x, int y) {
        if(x+1 < 0 || x + 1 >= spaces.length || y + 1 < 0 || y + 1 >= spaces[0].length){
            return new Space('O', new Coordinate(x,y));
        }
        return spaces[x+1][y+1];
    }

    /**
     * Gets the space at a given coordinate.
     *
     * @param coord the coordinate on the board
     * @return the space at that coordinate
     */
    public Space getSpace(Coordinate coord) {
        return getSpace(coord.row, coord.col);
    }


    /**
     * Moves a worker from one coordinate to another.
     *
     * @param worker   The worker being moved
     * @param starting The workers starting location
     * @param ending   The workers ending location
     */
    public void moveWorker(Worker worker, Coordinate starting, Coordinate ending) {
        assert (getSpace(starting).getWorker() != null && worker.equals(getSpace(starting).getWorker()));
        assert (getSpace(ending).isTraversable());
        getSpace(starting).popWorker();
        getSpace(ending).addWorker(worker);
    }

    /**
     * Builds a piece (e.g. tower level) at the given location.
     *
     * @param piece    the piece to build
     * @param position where to build it
     */
    public void buildPiece(colorablePiece piece, Coordinate position) {
        // Checks if the space is buildable
        assert (getSpace(position).isBuildable());
        getSpace(position).addPiece(piece);
    }


    /**
     * Checks if a move from start to end is allowed based on tile properties.
     *
     * @param start the origin coordinate
     * @param end   the destination coordinate
     * @return true if the move is allowed
     */

    /**
     * Gets the dimensions of the playable board.
     *
     * @return the board's dimensions
     */
    public Dimensions getDimensions() {
        return dims;
    }


    /**
     * Prints the current state of the board to the console.
     */
    public void printBoard() {
        StringBuilder output = new StringBuilder();
        output.append("Current Board:\n");
        for (int x = 0; x < dims.row; x++) {
            for (int y = 0; y < dims.col; y++) {
                output.append(getSpace(x, y).toString());
            }
            output.append("\n");
        }
        System.out.println(output);
    }

    public class Dimensions {
        public final int row, col;
        public Dimensions(int x_max, int y_max) {
            row = x_max; col =y_max; }
        public int size() {return row * col;}
    }

    /**
     * Creates padding for the ocean
     */
    private void initOceanPadding() {
        for (int x = 0; x < spaces.length; x++) {
            for (int y = 0; y < spaces[x].length; y++) {
                if((x!=0 && x!=spaces.length-1) && y != 0){
                    y=spaces[x].length-1;
                }
                spaces[x][y] = new Space('O', new Coordinate(x-1, y-1));
            }
        }
    }

    /**
     * Identifies the perimeter tiles for edge detection and visuals
     */
    private void initPerimeter(){
        for(int x = 0; x < dims.row; x++){
            for(int y = 0; y < dims.col; y++){
                Coordinate[] adjacencies = spaces[x][y].coordinate.getOrthogonalAdjacencies();
                for(Coordinate neighbour : adjacencies){
                    if(getSpace(neighbour).getCreatePerimeter()){
                        getSpace(x, y).setPerimeter();
                        break;
                    }
                }
            }
        }
    }

    /**
     * Generates the playable ground for the game
     */
    private void initPlayableGround(){
        for (int row = 1; row <= dims.row; row++){
            for (int col = 1; col <= dims.col; col++){
                spaces[row][col] = new Space('.', new Coordinate(row - 1, col - 1));
            }
        }
    }


}
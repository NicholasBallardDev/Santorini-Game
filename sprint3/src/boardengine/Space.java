package boardengine;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

/**
 * Represents a single tile on the game board. A space con hold multiple terrain pieces, workers and has methods to find it's state.
 *
 */
public class Space {
    // Public
    /**
     * The position of this space on the board.
     */
    public final Coordinate coordinate;

    // Private
    private Worker worker;
    private Stack<colorablePiece> pieces = new Stack<>();
    private boolean perimeter = false;
    JButton tileButton;

    /**
     * Creates a space with the given type and position.
     *
     * @param character the symbol representing the tile type ('O' for ocean, '.' for ground)
     * @param coord     the coordinate of this space
     */
    public Space(char character, Coordinate coord) {
        coordinate = coord;

        switch (character) {
            case 'O':
                addPiece(new Ocean());
                break;
            case '.':
                addPiece(new Ground());
                break;
            default:
                assert (false); // invalid character
        }

        this.tileButton = new JButton();
        refreshTile();

    }

    /**
     * Adds a piece (e.g., ground, tower, dome) to this space.
     *
     * @param piece the piece to add
     */
    public void addPiece(colorablePiece piece) {
        pieces.push(piece);
    }

    /**
     * Removes the top piece from the space.
     */
    public void removePiece() {
        pieces.pop();
    }

    /**
     * Marks this space as a perimeter tile.
     *
     * @return the same space (for chaining if needed)
     */
    public Space setPerimeter() {
        perimeter = true;
        return this;
    }

    /**
     * Getter function for Coordinate
     *
     * @return Coordinate of the space
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Checks whether this space is marked as a perimeter.
     *
     * @return true if it is on the perimeter
     */
    public boolean isPerimeter() {
        return perimeter;
    }

    /**
     * Returns the worker on this space.
     * Will throw an assertion error if no worker is present.
     *
     * @return the worker
     */
    public Worker getWorker() {
        assert (hasWorker());
        return worker;
    }

    /**
     * Returns the name of the topmost piece on the space.
     *
     * @return name of the top piece
     */
    public String getName() {
        return getTopPiece().name;
    }

    /**
     * Checks whether the space has a worker
     *
     * @return true if a worker is present
     */
    public boolean hasWorker() {
        return worker != null;
    }

    /**
     * Checks if this space can be moved onto (no obstacles, traversable terrain).
     *
     * @return true if it can be moved onto
     */
    public boolean isTraversable() {
        if (worker != null && !worker.isTraversable()) return false;
        return getTopPiece().isTraversable();
    }


    /**
     * Returns the build height of the space (excluding the base terrain).
     *
     * @return number of pieces stacked, minus base tile
     */
    public int getHeight() {
        return pieces.size() - 1;
    }

    /**
     * Returns the display character for this space.
     * Shows the worker if present; otherwise shows the top piece.
     *
     * @return a character representing the space
     */
    @Override
    public String toString() {
        if (hasWorker()) return String.valueOf(getWorker().getChar());
        return String.valueOf(getTopPiece().getChar());
    }

    /**
     * Removes the worker from this space and returns it.
     *
     * @return the removed worker
     */
    public Worker popWorker() {
        assert (worker != null);
        Worker tempWorker = worker;
        worker = null;
        return tempWorker;
    }

    /**
     * Places a worker onto this space.
     *
     * @param worker the worker to add
     */
    public void addWorker(Worker worker) {
        assert (this.worker == null);
        this.worker = worker;
    }

    /**
     * Gets the tile type of the topmost piece.
     *
     * @return the tile type
     */
    public TileType getTileType() {
        return getTopPiece().tileType;
    }

    /**
     * Checks if a new piece can be built on this space.
     *
     * @return true if buildable
     */
    public boolean isBuildable() {
        return getTopPiece().isBuildable();
    }

    /**
     * Checks if this space should be treated as a perimeter tile.
     * Used during board initialization.
     *
     * @return true if it triggers perimeter marking
     */
    public boolean getCreatePerimeter() {
        return getTopPiece().createPerimeter;
    }

    public void refreshTile(){
        this.tileButton = new JButton();
        tileButton.setPreferredSize(new Dimension(80,80));
        tileButton.setEnabled(true);
        tileButton.setBackground(getTopPiece().getTileColor());
        if(getHeight() > 0){ tileButton.setText(String.valueOf(getHeight())); }

        if (hasWorker()) {
            tileButton.setIcon(getWorker().getIcon());
            tileButton.setHorizontalAlignment(SwingConstants.CENTER);
            tileButton.setVerticalAlignment(SwingConstants.CENTER);
            tileButton.setDisabledIcon(getWorker().getIcon());
        }
    }

    /**
     * Visually marks the worker if it belongs to the current player.
     */


    public JButton getTileButton() {
        return tileButton;
    }

    // ##### PRIVATE
    private colorablePiece getTopPiece(){
        assert(pieces.peek() != null);
        return pieces.peek();
    }

}

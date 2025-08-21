package pieces;

import boardengine.Coordinate;

/**
 * Base class for all board pieces.
 * Defines common properties such as whether the piece is traversable or buildable.
 */
public abstract class Piece {

    /**
     * Whether placing this piece should trigger a perimeter update.
     */
    public boolean createPerimeter = false;

    /**
     * Display name of the piece. Defaults to "Piece", should be overridden.
     */
    public String name = "Piece";

    /**
     * The type of tile this piece represents.
     */
    public TileType tileType;

    /**
     * Indicates whether a worker can move onto this piece.
     *
     * @return true if traversable
     */
    public boolean isTraversable() {
        return traversable;
    }

    /**
     * Indicates whether a new piece can be built on top of this one.
     *
     * @return true if buildable
     */
    public boolean isBuildable() {
        return buildable;
    }

    /**
     * Indicates whether this piece can be removed (e.g. during undo or reset).
     *
     * @return true if removable
     */
    public boolean isRemovable() {
        return removable;
    }

    /**
     * Returns the character representation of the piece for display on the board.
     *
     * @return character representing the piece
     */
    public abstract char getChar();

    // Protected Fields

    protected boolean traversable = false;
    protected boolean buildable = false;
    protected boolean removable = false;

    protected Coordinate coordinate;
}

package pieces;

import java.awt.*;

/**
 * Represents a tower segment on the board.
 * Towers can be stacked up to a certain height and are both buildable and traversable.
 */
public class Tower extends colorablePiece {

    /**
     * Height of this tower segment
     */
    private final int height;

    /**
     * Creates a new tower segment with the given height.
     *
     * @param height the height level of this tower (1–3 typically)
     */
    public Tower(int height) {
        this.traversable = true;
        this.buildable = true;
        this.removable = true;
        this.name = "Tower";
        this.height = height;
        this.tileType = TileType.TOWER;
    }

    /**
     * Returns a character representing the tower's height.
     * Converts ints into chars
     *
     * @return character version of height
     */
    @Override
    public char getChar() {
        return (char) (height + 48); // ASCII conversion for digits
    }

    /**
     * Getter for the TileType
     * @return tileType
     */
    @Override
    public TileType getTileType() {
        return tileType;
    }

    /**
     * Getter for the color of the tile
     * @return Color
     */
    @Override
    public Color getTileColor() {
        int colorableHeight = height-1;
        return  new Color(255-(colorableHeight*80), 255-(colorableHeight*80), 255-(colorableHeight*80)) ;
    }
}

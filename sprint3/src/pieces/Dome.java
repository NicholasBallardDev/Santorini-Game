package pieces;

import java.awt.*;

/**
 * Represents the final piece that can be placed on top of a tower.
 * Domes are not traversable or buildable, and they complete the structure.
 */
public class Dome extends colorablePiece {

    /**
     * Creates a dome piece. Domes are not walkable, not buildable,
     * and cannot be removed once placed.
     */
    public Dome() {
        this.traversable = false;
        this.buildable = false;
        this.removable = false;
        this.name = "Dome";
        this.tileType = TileType.DOME;
    }

    /**
     * Returns the character used to display this dome on the board.
     *
     * @return 'D'
     */
    @Override
    public char getChar() {
        return 'D';
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
        return Color.BLUE;
    }
}

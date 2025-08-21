package pieces;

import java.awt.*;

/**
 * Represents an ocean tile, used as padding around the playable board area.
 * Ocean tiles are not walkable, not buildable, and cannot be removed.
 */
public class Ocean extends colorablePiece {

    /**
     * Creates an ocean tile.
     */
    public Ocean() {
        this.traversable = false;
        this.buildable = false;
        this.removable = false;
        this.createPerimeter = true;
        this.name = "Ocean";
        this.tileType = TileType.OCEAN;
    }

    /**
     * Returns the character used to represent an ocean tile.
     *
     * @return 'O'
     */
    @Override
    public char getChar() {
        return 'O';
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
        int variation = (int)(Math.random() * 30) - 15;
        int r = Math.max(0, Math.min(255, 95 + variation));
        int g = Math.max(0, Math.min(255, 200 + variation));
        int b = Math.max(0, Math.min(255, 190 + variation));
        return new Color(r, g, b);
    }
}

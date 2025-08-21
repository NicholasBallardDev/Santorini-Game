package pieces;

import java.awt.*;

/**
 * Represents a ground tile — the basic, walkable surface of the board.
 * Ground tiles can be built upon but cannot be removed.
 */
public class Ground extends colorablePiece {

    /**
     * Creates a ground tile. Ground is traversable, buildable, and permanent.
     */
    public Ground() {
        this.traversable = true;
        this.buildable = true;
        this.removable = false;
        this.name = "Ground";
        this.tileType = TileType.GROUND;
    }

    /**
     * Returns the character used to represent ground on the board.
     *
     * @return A character representing Ground
     */
    @Override
    public char getChar() {
        return '.';
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

        int r = Math.max(0, Math.min(255, 140 + variation));
        int g = Math.max(0, Math.min(255, 210 + variation));
        int b = Math.max(0, Math.min(255, 80 + variation));
        return new Color(r, g, b);
    }
}

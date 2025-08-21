package pieces;

import java.awt.*;

/**
 * abstract class for pieces that is 'colorable' when displayed to the screen
 */
public abstract class colorablePiece extends Piece {
    /**
     * Getter function for the character of the piece
     * @return character
     */
    public abstract char getChar();

    /**
     * getter function for the TileType the piece is
     * @return TileType
     */
    public abstract TileType getTileType();

    /**
     * Getter for the color of the tile
     * @return Color
     */
    public abstract Color getTileColor();
}

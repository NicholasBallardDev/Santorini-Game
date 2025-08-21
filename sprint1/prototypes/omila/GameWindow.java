package Sprint_1.Prototypes.Omila;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JPanel {

    // SCREEN PARAMETERS

    // Creates a 16x16 tile. Size of pieces in the game
    final int origionalTileSize = 16;
    final int scale = 3;

    final int tileSize = origionalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 16;

    // 776 pixel screen
    final int screenWidth = tileSize * maxScreenCol;
    // 776 pixel screen
    final int screenHeight = tileSize * maxScreenRow;


    public GameWindow(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);
    }



}

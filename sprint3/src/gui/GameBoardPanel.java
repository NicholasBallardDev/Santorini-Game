package gui;

import javax.swing.*;
import java.awt.*;
import java.util.*;


import actions.BuildAction;
import actions.MoveAction;
import boardengine.Board;
import boardengine.Space;
import gameengine.GameController;
import gameengine.GamePhase;
import pieces.Worker;


/**
 * The main panel that renders the game board UI.
 * Displays tiles, highlights valid actions, and handles clicks.
 */
public class GameBoardPanel extends JPanel{

    private final Board board;
    private final int tileSize = 80;
    private final GameController controller;

    /**
     * Constructs a panel representing the Santorini game board.
     *
     * @param board the game board to render
     * @param controller the game controller for logic interaction
     */
    public GameBoardPanel(Board board,GameController controller){
        this.board = board;
        this.controller = controller;


        // Getting the width of the board  + ocean
        int boardWidth = board.getDimensions().row + 2;
        // Getting the Height of the board + Ocean
        int boardHeight = board.getDimensions().col + 2;

        setLayout(new GridLayout(boardWidth, boardHeight));
        drawBoard();

        SwingUtilities.invokeLater( () -> {
            welcomeMessage();
            startTurnPlayerMessage();
        });
    }


    /**
     * Repaints the board by drawing each tile and setting its icon, color, and listener.
     */
    public void drawBoard(){
        removeAll();
        revalidate();
        repaint();

        Space[][] spaces = getBoardSpaces();

        for (int row = 0; row < spaces.length; row++) {
            for (int col = 0; col < spaces[row].length; col++) {
                Space space = spaces[row][col];

                space.refreshTile();
                JButton tileButton = space.getTileButton();

                if(space.hasWorker()){
                    applyWorkerHighlight(tileButton, space.getWorker());
                }

                // Movement Highlighting
                Color highlight = moveHighlight(row, col);
                if (highlight != null) {
                    tileButton.setBackground(highlight);
                }

                int finalRow = row - 1;
                int finalCol = col - 1;
                tileButton.addActionListener(e -> {
                    controller.handleTileClick(finalRow, finalCol);
                    drawBoard();
                });

                add(tileButton);
        }
        }
    }


    /**
     * Helper to fetch board state with ocean padding.
     */
    private Space[][] getBoardSpaces(){
        int totalWidth = board.getDimensions().row + 2;
        int totalHeight = board.getDimensions().col + 2;
        Space[][] boardSpaces = new Space [totalWidth][totalHeight];

        for (int row = 0; row < totalWidth; row++){
            for(int col = 0; col < totalHeight; col++){
                boardSpaces[row][col] = board.getSpace(row - 1,col - 1);
            }
        }
        return boardSpaces;
    }

    /**
     * Highlights valid move/build options for the selected worker.
     */
    private Color moveHighlight(int row, int col){

        if(controller.getSelectedWorker() == null){
            return null;
        }


        ArrayList<MoveAction> possibleMoves = controller.getPossibleMoves();
        ArrayList<BuildAction> possibleBuilds = controller.getPossibleBuilds();

        if(controller.getCurrentPhase() == GamePhase.MOVE && possibleMoves != null){
            for (MoveAction move : possibleMoves){
                if(move.getMoveEnd().row == (row - 1) && move.getMoveEnd().col == (col - 1)){
                    return move.isValid() ? new Color(255,255,180) : new Color(255,100,100);
                }
            }
        } else if (controller.getCurrentPhase() == GamePhase.BUILD && possibleBuilds != null){
            for (BuildAction builds : possibleBuilds){
                if(builds.position.row == (row - 1) && builds.position.col == (col - 1)){
                    return builds.isValid() ? new Color(255, 200, 130) : new Color(255,100,100);
                }
            }
        }
return null;
    }


    /**
     * Visually marks the worker if it belongs to the current player.
     */
    private void applyWorkerHighlight(JButton tileButton, Worker worker) {
        if (controller.getCurrentPlayer().ownsWorker(worker)) {
            tileButton.setBorder(BorderFactory.createLineBorder(new Color(255, 223, 100), 3));
        } else {
            tileButton.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    /**
     * Displays a welcome message when the game starts.
     */
    private void welcomeMessage(){
        JOptionPane.showMessageDialog(
                this,
                "Welcome to Santorini!",
                "Santorini Game",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Displays the current player's turn info and god card on start.
     */
    private void startTurnPlayerMessage(){
        JOptionPane.showMessageDialog(null,
                "   It's" +controller.getCurrentPlayer().getName() + "'s turn!"  + " \n   This is your God Card: " + controller.getCurrentPlayer().getGod().getName() + "\n  Select your Worker to move them!",
                "Game Start",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    /**
     * Returns a random integer within a range for subtle visual variation.
     */
    private int rand(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

//    case GROUND -> tileButton.setBackground(new Color(140 + rand(-15, 15), 210 + rand(-15, 15), 80 + rand(-15, 15)));
//    case OCEAN -> tileButton.setBackground(new Color(95 + rand(-15, 15), 200 + rand(-15, 15), 190 + rand(-15, 15)));
}

package gui;

import boardengine.Board;
import gameengine.GameController;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window for the Santorini game.
 * Uses a CardLayout to manage different screens like the main menu, new game setup, and game screen.
 */
public class Main extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    /**
     * Initializes the main window and sets up all the screens.
     */
    public Main() {
        setTitle("Santorini Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Screens
        cardPanel.add(new MainMenuPanel(this), "MainMenu");
        cardPanel.add(new NewGamePanel(this), "NewGame");

        add(cardPanel);
        showPanel("MainMenu");

        setVisible(true);
    }

    /**
     * Shows the screen associated with the given name.
     *
     * @param name the name of the screen (e.g., "MainMenu", "NewGame")
     */
    public void showPanel(String name) {
        cardLayout.show(cardPanel, name);
    }


    /**
     *
     * @param board
     * @param gameController
     */
    public void startNewGame(Board board, GameController gameController, TimerPanel timerPanel) {
        cardPanel.add(new GamePanel(new GameBoardPanel(board, gameController), timerPanel), "GamePanel");
        showPanel("GamePanel");

    }

    /**
     * Entry point of the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}

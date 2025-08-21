package gui;

import boardengine.Board;
import gameengine.Game;
import gameengine.GameController;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for starting a new game.
 * Lets the user choose the number of players and begin a fresh game session.
 */
public class NewGamePanel extends JPanel {

    // Dimensions for the playable board ( 5x5 by default )
    private int playableWidth = 5;
    private int playableHeight = 5;

    /**
     * Creates the New Game screen with UI elements to configure and start a new game.
     *
     * @param frame reference to the main application frame (used for screen switching)
     */
    public NewGamePanel(Main frame) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        setBackground(new Color(220, 230, 250));

        // Title
        JLabel titleLabel = new JLabel("Start New Game!");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Player selection label
        JLabel playerLabel = new JLabel("Select number of players");
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Dropdown for selecting 2 or 3 players
        String[] playerOptions = {"2", "3"};
        JComboBox<String> playerDropdown = new JComboBox<>(playerOptions);
        playerDropdown.setMaximumSize(new Dimension(200, 30));
        playerDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start Game button
        JButton startGameButton = new JButton("Start Game");
        startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Back to Main Menu button
        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start Game button logic
        startGameButton.addActionListener(e -> {
            int numberOfPlayer = Integer.parseInt((String) playerDropdown.getSelectedItem());

            // Create game components
            Board board = new Board(playableWidth, playableHeight);
            Game game = new Game(board, numberOfPlayer, frame);
            TimerPanel timerPanel = new TimerPanel(game, game.getCurrentPlayer().playerID, 5,0);
            GameController gameController = new GameController(game, board, timerPanel);


            // Switch to the game screen
            frame.startNewGame(board, gameController, timerPanel);
        });

        // Back button logic
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));

        // Add all components to the panel
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 40)));
        add(playerLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(playerDropdown);
        add(Box.createRigidArea(new Dimension(0, 40)));
        add(startGameButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(backButton);
    }
}

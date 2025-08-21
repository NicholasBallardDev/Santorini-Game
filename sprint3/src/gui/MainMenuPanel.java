package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Main menu screen shown when the game starts.
 * Allows the player to start a new game or quit the application.
 */
public class MainMenuPanel extends JPanel {

    /**
     * Creates the main menu panel with buttons to start or quit the game.
     *
     * @param frame reference to the main application frame (used for switching panels)
     */
    public MainMenuPanel(Main frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(220, 230, 250));
        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Title label
        JLabel titleLabel = new JLabel("Santorini Game");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // New Game button
        JButton newGameButton = new JButton("New Game");
        newGameButton.setMaximumSize(new Dimension(200, 40));
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(e -> frame.showPanel("NewGame"));

        // Load Game button (not wired up yet)
        JButton loadGameButton = new JButton("Load Game");

        // Quit button
        JButton quitButton = new JButton("Quit Game");
        quitButton.setMaximumSize(new Dimension(200, 40));
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.addActionListener(e -> System.exit(0));

        // Add components to the panel
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(newGameButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        // Add placeholder for loadGameButton if needed later
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(quitButton);
    }
}

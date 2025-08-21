package pieces.workerfeatures;

import javax.swing.*;
import java.awt.*;

/**
 * Factory class for generating {@link WorkerAppearance} objects based on player ID.
 * Each player is assigned a unique color and icon set.
 */
public class WorkerAppearanceFactory {

    /**
     * Returns a WorkerAppearance based on the given player ID.
     * Provides distinct colors and icons for each player to differentiate them visually.
     *
     * @param playerID the ID of the player (e.g. 0, 1, or 2)
     * @return a configured WorkerAppearance for the player
     */
    public static WorkerAppearance getAppearanceForPlayer(int playerID) {
        switch (playerID) {
            case 0:
                return new WorkerAppearance(
                        Color.CYAN,
                        Color.BLUE,
                        new ImageIcon(ClassLoader.getSystemResource("assets/workericons/female_worker_red.png")),
                        new ImageIcon(ClassLoader.getSystemResource("assets/workericons/male_worker_red.png")),
                        80
                );
            case 1:
                return new WorkerAppearance(
                        Color.MAGENTA,
                        Color.PINK,
                        new ImageIcon(ClassLoader.getSystemResource("assets/workericons/female_worker_blue.png")),
                        new ImageIcon(ClassLoader.getSystemResource("assets/workericons/male_worker_blue.png")),
                        80
                );
            case 2:
                return new WorkerAppearance(
                        new Color(204, 102, 0),
                        new Color(255, 128, 0),
                        new ImageIcon(ClassLoader.getSystemResource("assets/workericons/female_worker_brown.png")),
                        new ImageIcon(ClassLoader.getSystemResource("assets/workericons/male_worker_brown.png")),
                        80
                );
            default:
                // Fallback style if the player ID is unexpected
                return new WorkerAppearance(
                        Color.GRAY,
                        Color.DARK_GRAY,
                        new ImageIcon(ClassLoader.getSystemResource("assets/workericons/FemaleWorkerBlack.png")),
                        new ImageIcon(ClassLoader.getSystemResource("assets/workericons/MaleWorkerBlack.png")),
                        80
                );
        }
    }
}

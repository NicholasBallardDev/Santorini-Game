package pieces.workerfeatures;

import javax.swing.*;
import java.awt.*;

/**
 * Stores appearance details for a player's worker, including colors and icons
 * for both male and female workers. Also handles scaling based on tile size.
 */
public class WorkerAppearance {

    private final Color maleColor;
    private final Color femaleColor;
    private final ImageIcon maleIcon;
    private final ImageIcon femaleIcon;
    private final int tileSize;

    /**
     * Creates a new appearance for a worker with different visuals
     * based on gender.
     *
     * @param femaleColor color used for female workers
     * @param maleColor color used for male workers
     * @param femaleIcon icon used for female workers
     * @param maleIcon icon used for male workers
     * @param tileSize the desired size for rendering icons
     */
    public WorkerAppearance(Color femaleColor, Color maleColor, ImageIcon femaleIcon, ImageIcon maleIcon, int tileSize) {
        this.femaleColor = femaleColor;
        this.maleColor = maleColor;
        this.tileSize = tileSize;
        this.femaleIcon = scaleIcon(femaleIcon);
        this.maleIcon = scaleIcon(maleIcon);
    }

    /**
     * Returns the color associated with the worker's gender.
     *
     * @param female true if the worker is female
     * @return the appropriate color
     */
    public Color getColor(boolean female) {
        return female ? femaleColor : maleColor;
    }

    /**
     * Returns the icon associated with the worker's gender.
     *
     * @param female true if the worker is female
     * @return the scaled icon image
     */
    public ImageIcon getIcon(boolean female) {
        return female ? femaleIcon : maleIcon;
    }

    /**
     * Scales the provided icon to fit the tile size.
     *
     * @param originalIcon the original image icon
     * @return a scaled version of the icon
     */
    private ImageIcon scaleIcon(ImageIcon originalIcon) {
        if (originalIcon == null) {
            return null;
        }

        // Take the origional image
        Image originalImage = originalIcon.getImage();
        // Scale the image
        Image scaledImage = originalImage.getScaledInstance(tileSize-40, tileSize-40, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}

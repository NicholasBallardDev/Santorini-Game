package gameengine;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents the timer that tracks the amount of time the player has left
 */
public class PlayerTimer implements ActionListener {
    private final JLabel countdownLabel;
    private int minutes;
    private int seconds;
    private final int playerId;
    private final Timer timer;
    private String countdown;

    Game game;

    /**
     * Instanciates Player Timer
     * @param game the game that is taking place
     * @param panel the panel we are adding the timer to
     * @param playerId the player this timer is for
     * @param minutes the number of minutes added to the timer
     * @param seconds the number of seconds added to the timer
     */
    public PlayerTimer(Game game, JPanel panel, int playerId, int minutes, int seconds) {
        timer  = new Timer(1000, this);
        this.playerId = playerId+1;
        this.game = game;
        this.countdown = String.format("Player %s: %02d:%02d",this.playerId, minutes, seconds);
        this.countdownLabel = new JLabel(countdown);
        panel.add(countdownLabel);
        this.minutes = minutes;
        this.seconds = seconds;

    }

    /**
     * Starts the timer
     */
    public void start(){
        timer.start();
    }

    /**
     * stops the timer
     */
    public void stop(){
        timer.stop();
    }

    /**
     * Counts down the timer
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        countdownLabel.setText(countdown);
        if(minutes == 0 && seconds == 0){
            System.out.println("Done");
            game.showVictoryDialog("Player " + (playerId == 1 ? "2" : "1"));
            timer.stop();
        }
        else if (seconds == 0) {
            seconds = 59;
            minutes--;
        }
        else{
            seconds--;
        }

        countdown = String.format("Player %s %02d:%02d",playerId, minutes, seconds);

    }
}


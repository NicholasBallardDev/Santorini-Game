package gui;

import gameengine.Game;
import gameengine.PlayerTimer;
import playerelements.Player;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Represents the TimerPanel to display the time each player has to complete their turns
 */
public class TimerPanel extends JPanel {
    PlayerTimer countdownTimer1;
    PlayerTimer countdownTimer2;
    int currentPlayer;
    int minutes;
    int seconds;
    Game game;
    ArrayList<PlayerTimer> countdownTimers;

    /**
     * Instantiates TimerPanel
     * @param game the game that is taking place
     * @param currentPlayer an integer representing the ID of the current player
     * @param minutes the number of minutes added to the timer
     * @param seconds the number of seconds added to the timer
     */
    public TimerPanel(Game game, int currentPlayer, int minutes, int seconds) {
        this.game = game;
        this.currentPlayer = currentPlayer;
        this.minutes = minutes;
        this.seconds = seconds;
        this.countdownTimers = new ArrayList<>();
        loadCountdown();
    }

    /**
     * Starts the countdown timer for the current player
     */
    private void loadCountdown() {
        Player[] players = game.getPlayers();
        for (Player player : players) {
            countdownTimers.add(new PlayerTimer(game, this, player.getPlayerID(), minutes, seconds));
        }

        countdownTimers.get(currentPlayer).start();
    }

    /**
     * Stops the current player's timer, and starts the next player's timer
     */
    public void switchTurns() {
        countdownTimers.get(currentPlayer).stop();
        nextPlayer();
        countdownTimers.get(currentPlayer).start();
    }

    private void nextPlayer(){
        currentPlayer++;
        if (currentPlayer > countdownTimers.size() - 1) {
            currentPlayer = 0;
        }
    }
}

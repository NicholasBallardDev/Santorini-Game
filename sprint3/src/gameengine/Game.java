package gameengine;

import actions.Action;
import boardengine.Board;
import boardengine.Coordinate;
import boardengine.Space;
import gui.Main;
import pieces.TileType;
import pieces.Worker;
import pieces.workerfeatures.WorkerAppearanceFactory;
import playerelements.gods.Artemis;
import playerelements.gods.Demeter;
import playerelements.gods.God;
import playerelements.Player;
import playerelements.gods.Zeus;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Main engine for running the game.
 * Handles player setup, god assignments, the main game loop, and win/loss conditions.
 */
public class Game {

    private final Board board;
    private Player[] players;
    private int nextPlayer = 0;
    private final Main frame;

    /**
     * Sets up a new game with the given board and number of players.
     * Randomly assigns gods and places workers.
     *
     * @param board The game board
     * @param numberOfPlayers Number of players participating
     */
    public Game(Board board, int numberOfPlayers, Main frame) {
        this.board = board;
        this.frame = frame;
        God[] gods = initGods();
        players = new Player[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player(i, gods[i], board, WorkerAppearanceFactory.getAppearanceForPlayer(i));
        }
        addRandomlyPlacedWorkers();
    }

    public Player[] getPlayers() {
        return players;
    }

    /**
     * Starts the game loop and prints out the winner.
     */
    // Should we use this??
    public void startGame() {
        Player winner = gameLoop();
        System.out.println("Congratulations for winning " + winner.name + "!");
    }

    public void showVictoryDialog(String winnerName) {
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    winnerName + " WINS!!! \nWhat would you like to do?",
                    "Victory!",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Exit to Menu", "Close Game"},
                    "Exit to Menu"
            );

            if (choice == 0) {
                SwingUtilities.invokeLater(() -> {
                    frame.showPanel("MainMenu");
                });
            } else if (choice == 1) {
                System.exit(0);
            }
        });
    }


    /**
     * Returns the current game board.
     *
     * @return the board instance
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Returns the player whose turn it currently is.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players[nextPlayer];
    }

    /**
     * Moves to the next player's turn.
     */
    public void nextTurn() {
        nextPlayer++;
        if (nextPlayer >= players.length) {
            nextPlayer = 0;
        }
    }

    /**
     * Returns a list of all available ground tiles without workers.
     *
     * @return list of coordinates for valid worker placement
     */
    public List<Coordinate> getAvailableGround() {
        List<Coordinate> availableGround = new ArrayList<>();
        for (int row = 0; row < board.getDimensions().row; row++) {
            for (int col = 0; col < board.getDimensions().col; col++) {
                Space space = board.getSpace(row, col);
                if (space.getTileType() == TileType.GROUND && !space.hasWorker()) {
                    availableGround.add(new Coordinate(row, col));
                }
            }
        }
        return availableGround;
    }

    /**
     * Places all players' workers on the board in random valid positions.
     * Assumes each player has exactly 2 workers.
     */
    public void addRandomlyPlacedWorkers() {
        List<Coordinate> availableGround = getAvailableGround();
        Collections.shuffle(availableGround);
        int requiredSpots = players.length * 2;

        if (availableGround.size() < requiredSpots) {
            throw new IllegalStateException("Not enough available ground to place all workers");
        }

        int index = 0;
        for (Player player : players) {
            for (Worker worker : player.getWorkers()) {
                Coordinate coord = availableGround.get(index++);
                worker.setCoordinate(coord);
            }
            player.setHasPlacedWorker(true);
        }
    }

    // #### Private
    /**
     * Starts the main game loop and returns the winning player.
     *
     * @return the player who won
     */
    private Player gameLoop() {
        while (true) {
            Player player = getNextPlayer();
            boolean winnerFound = player.completeTurn();
            if (winnerFound) return player;

            players = updatePlayersRemaining();
            Player winner = findWinner();
            if (winner != null) return winner;
        }
    }

    /**
     * Retrieves and cycles to the next player.
     * Outputs the next player who's turn is coming up
     *
     * @return the next player
     */
    private Player getNextPlayer() {
        nextPlayer++;
        if (nextPlayer >= players.length) nextPlayer = 0;
        return players[nextPlayer];
    }

    /**
     * Returns an array of all players except the given one.
     *
     * @param player the player to exclude
     * @return list of opponents
     */
    private Player[] getOtherPlayers(Player player) {
        Player[] opponents = new Player[players.length - 1];
        int index = 0;
        for (Player p : players) {
            if (!p.equals(player)) {
                opponents[index++] = p;
            }
        }
        return opponents;
    }

    /**
     * Filters out eliminated players and returns a new array of remaining players.
     *
     * @return array of remaining players
     */
    private Player[] updatePlayersRemaining() {
        int count = 0;
        for (Player player : players) {
            if (!player.isEliminated()) count++;
        }
        Player[] output = new Player[count];
        int index = 0;
        for (Player player : players) {
            if (!player.isEliminated()) {
                output[index++] = player;
            }
        }
        return output;
    }

    /**
     * Returns the winner if one is found; otherwise null.
     *
     * @return the winning player or null
     */
    private Player findWinner() {
        if (players.length == 1) return players[0];
        for (Player player : players) {
            if (player.isWinner()) return player;
        }
        return null;
    }

    /**
     * Randomly assigns two god powers to the players.
     *
     * @return array of assigned gods
     */
    private God[] initGods() {
        God[] gods = {new Zeus(), new Demeter()};
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        for (int i = gods.length - 1; i > 0; i--) {
            int swapIndex = random.nextInt(i + 1);
            God temp = gods[swapIndex];
            gods[swapIndex] = gods[i];
            gods[i] = temp;
        }
        return gods;
    }

    /**
     * Converts a string name to a God instance.
     *
     * @param name name of the god
     * @return the matching God object, or null if not found
     */
    private God stringToGod(String name) {
        return switch (name) {
            case "Artemis" -> new Artemis();
            case "Demeter" -> new Demeter();
            default -> null;
        };
    }

    /**
     * Flattens a 2D array of actions into a single list.
     *
     * @param array the 2D array of actions
     * @return flat array of all actions
     */
    // This most definetly could have been used more
    private Action[] flattenArray(Action[][] array) {
        int length = 0;
        for (Action[] actions : array) {
            length += actions.length;
        }
        Action[] output = new Action[length];
        int position = 0;
        for (Action[] actions : array) {
            System.arraycopy(actions, 0, output, position, actions.length);
            position += actions.length;
        }
        return output;
    }
}

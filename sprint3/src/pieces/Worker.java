package pieces;

import actions.Action;
import actions.BuildAction;
import actions.MoveAction;
import actions.PlaceWorkerAction;
import boardengine.Board;
import boardengine.Coordinate;
import boardengine.Space;
import pieces.workerfeatures.WorkerAppearance;
import playerelements.History;
import playerelements.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a worker on the board. They are controlled by players and can move, build and interact with game elements.
 */
public class Worker extends Piece {

    /** Whether this worker is female */
    public final boolean female;

    /** ID of the player who owns this worker. */
    public final Player owner;
    public final int ownerID;

    private History history;
    private boolean active;
    private final WorkerAppearance appearance;
    private final Board board;

    /**
     * Creates a new worker with given owner ID, gender, and appearance.
     *
     * @param owner the player who owns this worker
     * @param female whether the worker is female
     * @param appearance the visual representation of the worker
     */
    public Worker(Player owner, boolean female, Board board, WorkerAppearance appearance) {
        this.female = female;
        this.board = board;
        this.owner = owner;
        this.appearance = appearance;
        this.traversable = false;
        this.buildable = false;
        this.removable = false;
        this.name = "Worker";
        this.coordinate = null;
        this.history = new History();
        this.active = true;
        this.ownerID = owner.getPlayerID();
    }

    /**
     * Returns a character used to represent this worker on the board.
     *
     * @return Returns the character
     */
    @Override
    public char getChar() {
        return ownerID == 0 ? 'A' : 'D';
    }

    /**
     * A boolean to check of the worker is female
     *
     * @return a Boolean
     */
    public Boolean isFemale() {
        return female;
    }

    /**
     * A checker to see if a worker is Active
     *
     * @return a boolean
     */
    public boolean isActive() {
        return active;
    }

    /**
     * A setter for active
     * @param act Sets the boolean
     */
    public void setActive(boolean act) {
        active = act;
    }

    /**
     * Updates the worker's coordinate on the board.
     * If this is the first placement, the worker is added to the space directly.
     * Otherwise, the worker is moved from their current location.
     *
     * @param coord the destination coordinate
     */
    public void setCoordinate(Coordinate coord) {
        Space space = board.getSpace(coord);
        if (this.coordinate == null) {
            space.addWorker(this);
        } else {
            board.moveWorker(this, this.coordinate, coord);
        }
        this.coordinate = coord;
    }

    /**
     * Gets the worker's color based on gender and player ID.
     *
     * @return the assigned color
     */
    public Color getColor() {
        return appearance.getColor(female);
    }

    /**
     * Gets the icon representing this worker.
     *
     * @return the appropriate scaled ImageIcon
     */
    public Icon getIcon() {
        return appearance.getIcon(female);
    }

    /**
     * Generates valid placement actions for this worker on all traversable tiles.
     *
     * @param board the game board
     * @return an array of valid placement actions
     */
    public ArrayList<PlaceWorkerAction> getPlaceWorkerActions(Board board) {
        ArrayList<PlaceWorkerAction> actions = new ArrayList<>();
        int count = 0;

        // Gets coordinates of the board that traversible
        for (int row = 0; row < board.getDimensions().row; row++) {
            for (int col = 0; col < board.getDimensions().col; col++) {
                if (!board.getSpace(row, col).isTraversable()) continue;
                Coordinate coord = new Coordinate(row, col);
                actions.add(new PlaceWorkerAction(this, coord));
            }
        }

        // Places the worker there
        return actions;
    }

    /**
     * Returns all possible move actions from the current position.
     *
     * @param board the game board
     * @return an array of move actions (valid or invalid)
     */
    public ArrayList<MoveAction> getMoveActions(Board board) {
        Space space = board.getSpace(coordinate);
        Coordinate[] neighbours = coordinate.getAjacencies();

        // Creates new MoveActionlist
        ArrayList<MoveAction> actions = new ArrayList<>();

        // Adds the neighbour coordinates
        for (Coordinate neighbourCoord : neighbours) {
            actions.add(getMove(space, board.getSpace(neighbourCoord)));
        }

        return actions;
    }

    /**
     * Builds a list of build actions the worker can perform in adjacent spaces.
     *
     * @param board the game board
     * @return an ArrayList of build actions
     */
    public ArrayList<BuildAction> getBuildActions(Board board) {
        Coordinate[] neighbours = coordinate.getAjacencies();
        ArrayList<BuildAction> actions = new ArrayList<>();

        for (Coordinate neighbour : neighbours) {
            actions.add(getBuild(board.getSpace(neighbour)));
        }

        return actions;
    }

    /**
     * Generates build actions for your friend's worker
     * @param board the game board
     * @return an ArrayList of build actions
     */
    public ArrayList<BuildAction> getHelpfulBuildActions(Board board) {
        ArrayList<BuildAction> actions = new ArrayList<>();
        int friendID;

        if(this.female) {
            friendID = 0;
        } else {
            friendID = 1;
        }

        Worker friend = this.owner.getWorkers()[friendID];
        Coordinate[] friendNeighbours = friend.getCoordinate().getAjacencies();

        for (Coordinate neighbour : friendNeighbours) {
            actions.add(getBuild(board.getSpace(neighbour)));
        }

        return actions;
    }

    /**
     * Processes a move attempt between two spaces and determines validity.
     */
    private MoveAction getMove(Space start, Space end) {
        int heightDifference = end.getHeight() - start.getHeight();
        MoveAction move = new MoveAction(start.getWorker(), start, end, end.getHeight());

        // If the player moves to third floor
        if (move.height >= 3) {
            move.setAsWinCondition();
        }

        StringBuilder invalidReason = new StringBuilder("Invalid Move for Following Reasons:\n");
        boolean invalid = false;

        // if movement is greater than 2 floors
        if (move.heightDifference >= 2) {
            invalidReason.append("\t-Worker cannot jump up more than 2 blocks.\n");
            invalid = true;
        }
        if (end.hasWorker() && !end.getWorker().isTraversable()) {
            invalidReason.append("\t-Worker cannot move to another space with a worker on it.\n");
            invalid = true;
        }
        if (!end.isTraversable()) {
            invalidReason.append("\t-Worker cannot move to space with untraversable piece ").append(end.getName()).append(".\n");
            invalid = true;
        }

        if (invalid) move.setAsInvalid(invalidReason.toString());
        return move;
    }

    /**
     * Processes a build attempt at a given space and checks for validity.
     */
    private BuildAction getBuild(Space targetPosition) {
        int height = targetPosition.getHeight();
        colorablePiece buildPiece = height == 3 ? new Dome() : new Tower(height + 1);

        BuildAction build = new BuildAction(this, buildPiece, targetPosition.coordinate);

        // For a dome
        if (height == 4) {
            build.setAsInvalid("Cannot build on dome");
            return build;
        }

        StringBuilder invalidReason = new StringBuilder("Invalid Build for Following Reasons:\n");
        boolean invalid = false;

        if (targetPosition.hasWorker() && !targetPosition.getWorker().isBuildable()) {
            invalidReason.append("\t-Cannot build on top of this worker.\n");
            invalid = true;
        }

        if (!targetPosition.isBuildable()) {
            invalidReason.append("\t-Cannot build on top of piece ").append(targetPosition.getName()).append(".\n");
            invalid = true;
        }

        if (invalid) build.setAsInvalid(invalidReason.toString());
        return build;
    }

    /**
     * Executes the selected action and records it in history.
     *
     * @param selectedAction  the action chosen by the player
     */
    public void processTurn(Action selectedAction) {
        selectedAction.processAction(board);
        addToHistory(selectedAction);
    }

    private void addToHistory(Action action) {
        action.processHistory(history);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Space getSpace(){
        return board.getSpace(coordinate);
    }

    /**
     * Returns the action history for this worker.
     *
     * @return the worker's history
     */
    public History getHistory() {
        return history;
    }

    /**
     * Clears the worker's action history.
     */
    public void resetHistory() {
        history.reset();
    }
}

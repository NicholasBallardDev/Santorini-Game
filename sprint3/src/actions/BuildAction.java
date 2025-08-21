package actions;

import boardengine.Board;
import boardengine.Coordinate;
import pieces.Piece;
import pieces.Worker;
import pieces.colorablePiece;
import playerelements.History;

/**
 * A class representing the action of building a tower
 */
public class BuildAction extends Action {

    /**
     * Constructor for Build Action
     *
     * @param worker The worker performing the action
     * @param piece The piece being built
     * @param position The location the piece is being built at
     */
    public BuildAction(Worker worker, colorablePiece piece, Coordinate position) {
        super(worker);
        this.worker = worker;
        this.piece = piece;
        this.position = position;
    }

    /**
     * String prompt displaying if the action is valid or not
     *
     * @return A string prompt on whether the action is valid
     */
    @Override
    public String getStringPrompt() {
        return "Build piece " + piece.name + " at " + position.toString() + "." + (validAction ? "" : " (Invalid)");
    }

    /**
     * Processes the build action on the board for display and logic
     *
     * @param board Returns the action outcome on the board
     */
    @Override
    public void processAction(Board board) {
        board.buildPiece(piece, position);
    }

    /**
     * Processes the action and adds it to the history
     *
     * @param history History class for the action to be processed.
     */
    @Override
    public void processHistory(History history) {
        super.processHistory(history);
        history.addBuild(this);
    }


    /**
     * Returns the build location
     *
     * @return Returns the coordinate position of the build location
     */
    public Coordinate getBuildPosition() {
        return position;
    }

    /**
     *
     */
    // Private
    public final colorablePiece piece;
    public final Coordinate position;
}

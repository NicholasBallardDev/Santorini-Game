package actions;

import boardengine.Board;
import boardengine.Coordinate;
import boardengine.Space;
import pieces.Worker;
import playerelements.History;

/**
 * Represents the move made by a worker from one space to another. Able to take in starting and ending location and height to check for valid moveing locations.
 *
 */
public class MoveAction extends Action {
    public final Space start;
    public final Space end;
    public final int heightDifference;
    public final int height;
    /**
     * Constructor for move action. Creates a move action for a worker with given start, end and heights
     *
     * @param worker the worker performing the move
     * @param start the tile the worker starts on
     * @param end the tile the worker moves to
     * @param finalHeight the height of the destination tile
     */
    public MoveAction(Worker worker, Space start, Space end, int finalHeight) {
        super(worker);
        this.worker = worker;
        this.start = start;
        this.end = end;
        this.heightDifference = end.getHeight() - start.getHeight();
        this.height = finalHeight;
    }


    /**
     * Returns a message describing the move, including whether it's valid.
     *
     * @return String describing the move
     */
    @Override
    public String getStringPrompt() {
        return "Move worker from " + start.toString() + " to " + end.toString() + "." + (validAction ? "" : " (Invalid)");
    }

    /**
     * Moves the worker to the destination space on the board.
     *
     * @param board The game board
     */
    @Override
    public void processAction(Board board) {
        worker.setCoordinate(end.getCoordinate());
    }
    // For future implementation
//    @Override
//    public void undoAction(Board board) {
//        board.moveWorker(worker, end, start);
//    }

    /**
     * Adds this move to the game history.
     *
     * @param history The game history to update
     */
    @Override
    public void processHistory(History history) {
        super.processHistory(history);
        history.addWorkerMove(this);
    }

    /**
     * Gets the starting space of the move.
     *
     * @return start coordinate
     */
    public Coordinate getMoveStart() {
        return start.getCoordinate();
    }


    /**
     * Gets the destination space of the move.
     *
     * @return end coordinate
     */
    public Coordinate getMoveEnd() {
        return end.getCoordinate();
    }




}

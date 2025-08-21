package actions;

import boardengine.Board;
import boardengine.Coordinate;
import pieces.Worker;


/**
 * Action that places a worker onto the board at a given position.
 * Used during the initial setup phase or when introducing a new worker.
 */
public class PlaceWorkerAction extends Action {
    // Private
    private Worker worker;
    private Coordinate position;

    /**
     * Constructor: creates a new action to place a worker at a specific coordinate.
     *
     * @param worker   the worker to be placed
     * @param position the tile where the worker will be placed
     */
    public PlaceWorkerAction(Worker worker, Coordinate position) {
        super(worker);
        this.worker = worker;
        this.position = position;
    }


    /**
     * Returns a message describing the placement action.
     *
     * @return a prompt showing the player ID and placement location
     */
    @Override
    public String getStringPrompt() {
        return "Place worker that belongs to player " + (worker.ownerID + 1) + " at " + position + (validAction ? "" : " (Invalid)");
    }

    /**
     * Places the worker on the board. If the worker is female, the turn ends after placement.
     *
     * @param board the game board
     */
    @Override
    public void processAction(Board board) {
        worker.setCoordinate(position);
        if (worker.isFemale()) {
            this.setAsEndTurn();
        }
    }

//    @Override
//    public void undoAction(Board board) {
//        board.getSpace(position).popWorker();
//    }

}

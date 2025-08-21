package actions;

import boardengine.Board;
import pieces.Worker;

/**
 * Represents teh action that ends the current players turn in the game
 *
 */
public class EndTurnAction extends Action {

    /**
     * Constructs an EndTurnAction for the specified worker.
     *
     * @param worker The worker associated with this action
     */
    public EndTurnAction(Worker worker) {
        super(worker);
    }


    /**
     * Returns the text shown to the player for this action
     *
     * @return A simple prompt to end the turn
     */
    @Override
    public String getStringPrompt() {
        return "End turn now";
    }

    /**
     * Marks this action as an end-of-turn. No board changes
     *
     * @param board Board not used here
     */
    @Override
    public void processAction(Board board) {
        this.setAsEndTurn();
    }
}

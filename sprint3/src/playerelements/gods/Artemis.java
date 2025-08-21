package playerelements.gods;

import actions.Action;
import actions.EndTurnAction;
import actions.MoveAction;
import boardengine.Board;
import gameengine.GamePhase;
import pieces.Worker;
import playerelements.History;

import java.util.ArrayList;

/**
 * Artemis allows a worker to move twice in one turn,
 * but the second move cannot return to the space the worker started from.
 */
public class Artemis extends God {

    /**
     * Generates Artemis's special move actions.
     * Includes all valid second moves, excluding the original tile,
     * and always includes the option to end the turn early.
     *
     * @param worker the worker performing the move
     * @param board  the current game board
     * @return an array of valid follow-up move actions plus an EndTurnAction
     */
    @Override
    public ArrayList<Action> generateGodActions(Worker worker, Board board) {
        History history = worker.getHistory();
        MoveAction prev = history.getWorkersLastMove(worker);
        ArrayList<MoveAction> moveActions = worker.getMoveActions(board);
        ArrayList<Action> allActions = new ArrayList<>();

        for (MoveAction moveAction : moveActions) {
            // Can't move back to where the worker came from
            if (moveAction.getMoveEnd() == prev.getMoveStart()) {
                moveAction.setAsInvalid("");
            }
            allActions.add(moveAction);
        }

        return allActions;
    }

    /**
     * Checks if Artemis's power should activate.
     * It only applies if the worker has already moved once this turn.
     *
     * @param worker the worker being checked
     * @return true if the god power can be used
     */
    @Override
    public boolean godCondition(Worker worker) {
        History history = worker.getHistory();
        return history.hasWorkerMoved() && history.getMoveCount() < 2;
    }

    /**
     * @return the name "Artemis"
     */
    @Override
    public String getName() {
        return "Artemis";
    }

    @Override
    public GamePhase getGamePhase() {
        return GamePhase.BUILD;
    }

    /**
     * Artemis requires that the same worker perform both moves.
     *
     * @return true
     */
    @Override
    public boolean restrictToOneWorker() {
        return true;
    }

    /**
     * Describes Artemis's power to the player.
     *
     * @return a prompt string explaining the move-twice rule
     */
    @Override
    public String getGodActionsPrompt() {
        return "You can move twice this turn! \nBut not back to the space you started from!";
    }
}

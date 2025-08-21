package gameengine;

//import javax.swing.*;

import actions.Action;
import actions.BuildAction;
import actions.MoveAction;
import boardengine.Board;
import boardengine.Coordinate;
import boardengine.Space;
import gui.Main;
import gui.TimerPanel;
import pieces.Worker;
import playerelements.Player;

//import javax.swing.*;
import javax.swing.*;
import java.util.ArrayList;

public class GameController {

    private final Board board;
    private final Game game;
    private GamePhase currentPhase;
    private Player currentPlayer;
    private Worker selectedWorker;
    private ArrayList<MoveAction> possibleMoves = null;
    private ArrayList<BuildAction> possibleBuilds = null;
    private Worker lockedWorker = null;
    private boolean godPowerActive = false;
    private boolean godPowerPrompted = false;
    private final TimerPanel timerPanel;


    /**
     * Constructor for game controller to manage input and phase transitions.
     *
     * @param game the game instance
     * @param board the board instance
     */
    public GameController(Game game, Board board, TimerPanel timerPanel) {
        this.game = game;
        this.board = board;
        this.currentPlayer = game.getCurrentPlayer();
        this.currentPhase = GamePhase.MOVE;
        this.timerPanel = timerPanel;
    }

    /**
     * Called when a tile is clicked. Handles selection or performs move/build.
     *
     * @param row the row of the clicked tile
     * @param col the column of the clicked tile
     */
    public void handleTileClick(int row, int col) {
        Space clickedSpace = board.getSpace(row, col);

        if (clickedSpace.hasWorker() && currentPlayer.ownsWorker(clickedSpace.getWorker()) && lockedWorker == null) {
            handleSelectWorker(clickedSpace);
            return;
        }

        if (selectedWorker != null) {
            if (currentPhase == GamePhase.MOVE) {
                handleMoveWorker(clickedSpace);
            } else if (currentPhase == GamePhase.BUILD) {
                handleWorkerBuilds(clickedSpace);
            }
        } else if (currentPhase == GamePhase.MOVE) {
            JOptionPane.showMessageDialog(null,
                    currentPlayer.getName() + " please select your worker to move them!",
                    "Worker not selected",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }


    /**
     * Handles what happens when a player selects a worker.
     */
    private void handleSelectWorker(Space clickedSpace) {
        Worker worker = clickedSpace.getWorker();
        // If a worker is clicked again, remove everything to allow player to see board
        if (worker == selectedWorker) {
            selectedWorker = null;
            possibleMoves = null;
            possibleBuilds = null;
            return;
        }

        // If the space has a worker
        if (clickedSpace.hasWorker() && currentPlayer.ownsWorker(worker)) {
            // If the worker is locked
            if (lockedWorker != null && worker != lockedWorker) {
                JOptionPane.showMessageDialog(
                        null,
                        "You must use the same worker for your God Power!",
                        "Invalid Worker Selected",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            selectedWorker = worker;

            // Selecting for Move Phase
            if (currentPhase == GamePhase.MOVE) {
                if (currentPlayer.getGod() != null && currentPlayer.getGod().godCondition(worker)) {
                    applyGodMoveActions(worker);
                } else {
                    possibleMoves = worker.getMoveActions(board);
                }

                // Selection for the Build Phase
            } else if (currentPhase == GamePhase.BUILD && worker.getHistory().hasWorkerMoved()) {
                if (godPowerActive) {
                    applyGodBuildActions(worker);
                } else {
                    possibleBuilds = new ArrayList<>();
                    possibleBuilds.addAll(worker.getBuildActions(board));

                }
            }
        }
    }

    /**
     * Handles moving a selected worker to a clicked space.
     */
    private void handleMoveWorker(Space clickedSpace) {
        if (selectedWorker == null || possibleMoves == null) return;

        Coordinate destination = clickedSpace.coordinate;

        for (MoveAction move : possibleMoves) {
            if (move.getMoveEnd().equals(destination) && move.isValid()) {
                selectedWorker.processTurn(move);

                if (move.isWinCondition()) {
                    SwingUtilities.invokeLater(() -> game.showVictoryDialog(currentPlayer.getName()));
                    return;
                }

                if (tryApplyGodPower(selectedWorker, true)) return;

                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(null,
                                currentPlayer.getName() + ", it's time to build!",
                                "Build Phase",
                                JOptionPane.INFORMATION_MESSAGE)
                );

                currentPhase = GamePhase.BUILD;

                if(declareHelpfulToken()) {
                    possibleBuilds = selectedWorker.getHelpfulBuildActions(board);
                } else{
                    possibleBuilds = selectedWorker.getBuildActions(board);
                }

                return;
            }
        }

        JOptionPane.showMessageDialog(
                null,
                "You can't move there. Please select a valid tile.",
                "Invalid Move",
                JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Handles building with the selected worker on a clicked space.
     */
    private void handleWorkerBuilds(Space clickedSpace) {
        if (selectedWorker == null || possibleBuilds == null) return;

        Coordinate destination = clickedSpace.coordinate;

        for (BuildAction build : possibleBuilds) {
            if (build.getBuildPosition().equals(destination) && build.isValid()) {
                selectedWorker.processTurn(build);

                if (tryApplyGodPower(selectedWorker, false)) return;

                endTurn();
                return;
            }
        }

        JOptionPane.showMessageDialog(
                null,
                "You can't build there. Please select a valid tile.",
                "Invalid Build",
                JOptionPane.WARNING_MESSAGE
        );
    }


    /**
     * Ends the current player's turn and prepares for the next player.
     */
    private void endTurn() {
        selectedWorker.resetHistory();
        game.nextTurn();
        currentPlayer = game.getCurrentPlayer();

        JOptionPane.showMessageDialog(null,
                "   Now it's " + currentPlayer.getName() + "'s turn!\n   Your God: " +
                        currentPlayer.getGod().getName() + "\nSelect your Worker to move.",
                "New Turn",
                JOptionPane.INFORMATION_MESSAGE
        );

        selectedWorker = null;
        lockedWorker = null;
        godPowerActive = false;
        godPowerPrompted = false;
        possibleMoves = null;
        possibleBuilds = null;
        currentPhase = GamePhase.MOVE;
        timerPanel.switchTurns();
    }

    // Getters

    public GamePhase getCurrentPhase(){
        return currentPhase;
    }


    public Worker getSelectedWorker(){
        return selectedWorker;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public String getCurrentGod(){
        return currentPlayer.getGod().getName();
    }

    public Game getGame (){
        return game;
    }

    public ArrayList<MoveAction> getPossibleMoves(){
        return possibleMoves;
    }

    public ArrayList<BuildAction> getPossibleBuilds(){
        return possibleBuilds;
    }

    // Helper Methods



    // Filters only Move Actions
    private ArrayList<MoveAction> filterMoveActions(ArrayList<Action> actions) {
        ArrayList<MoveAction> moveList = new ArrayList<>();
        for (Action action : actions) {
            if (action instanceof MoveAction) {
                moveList.add((MoveAction) action);
            }
        }
        return moveList;
    }


    // Filters only Build Actions
    private ArrayList<BuildAction> filterBuildActions(ArrayList<Action> actions) {
        ArrayList<BuildAction> buildList = new ArrayList<>();
        for (Action action : actions) {
            if (action instanceof BuildAction) {
                buildList.add((BuildAction) action);
            }
        }
        return buildList;
    }


    // Applies GodMoves to possibleMoves
    private void applyGodMoveActions(Worker worker){
        if(worker.getHistory().hasWorkerMoved()){
            ArrayList<Action> godActions = currentPlayer.getGod().generateGodActions(worker, board);
            possibleMoves = filterMoveActions(godActions);
        }
    }


    // Applies god builds to possible Builds
    private void applyGodBuildActions(Worker worker){
        ArrayList<Action> godActions = currentPlayer.getGod().generateGodActions(worker,board);
        if(declareHelpfulToken()) {
            possibleBuilds = worker.getHelpfulBuildActions(board);
        } else{
            possibleBuilds = filterBuildActions(godActions);
        }


    }

    private boolean tryApplyGodPower(Worker worker, boolean isMovePhase) {

        // If god power has been prompted, return false.
        if(godPowerPrompted) return false;

        if (currentPlayer.getGod() != null && currentPlayer.getGod().godCondition(worker)) {
            // This is a horrible solution for an immediate problem
            godPowerPrompted = true;
            describeGodPower();
            if (promptUseGodPower()) {
                godPowerActive = true;
                currentPhase = currentPlayer.getGod().getGamePhase();
                if(currentPlayer.getGod().restrictToOneWorker()){lockedWorker = worker;}
                if (currentPhase == GamePhase.MOVE) {
                    applyGodMoveActions(worker);
                    return !possibleMoves.isEmpty();
                } else if (currentPhase == GamePhase.BUILD) {
                    applyGodBuildActions(worker);
                    return !possibleBuilds.isEmpty();
                }
            }
        }
        return false;
    }

    private boolean promptUseGodPower(){
        if(!currentPlayer.getGod().isOptional()){
            return true;
        }

        int response = JOptionPane.showConfirmDialog(
                null,
                "Do you want to use " + currentPlayer.getGod().getName() + "'s power?",
                currentPlayer.getGod().getName() + " Power",
                JOptionPane.YES_NO_OPTION
        );

        // If the Player selects to use the god power, output a prompt of what the god does.
        return response == JOptionPane.YES_OPTION;
    }

    /**
     * Displays a message about the Player's god power
     */
    private void describeGodPower(){
        JOptionPane.showMessageDialog(
                null,
                currentPlayer.getGod().getName() + "'s God Power: " + currentPlayer.getGod().getGodActionsPrompt(),
                currentPlayer.getGod().getName() + "God Power",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Prompts the player whether they want to use a Helpful token or not
     * @return a boolean representing if the player has or has not 'declared' their helpful token
     */
    private boolean declareHelpfulToken(){
        if(!currentPlayer.hasHelpfulToken()){
            return false;
        }

        int response = JOptionPane.showConfirmDialog(
                null,
                "You have " + String.valueOf(currentPlayer.getHelpfulTokenNumber()) + " helpful tokens"
                + "\n Would you like to use 1 of them to allow your other worker to build?",
                "Helpful Token",
                JOptionPane.YES_NO_OPTION
        );


        // If the Player selects to use the god power, output a prompt of what the god does.
        if(response == JOptionPane.YES_OPTION){
            currentPlayer.useHelpfulToken();
            return true;
        }
        return false;
    }

//    private boolean confirmMove(){
//        int result = JOptionPane.showConfirmDialog(
//                null,
//                "Would you like to move here?",
//                "Confirm Move",
//                JOptionPane.YES_NO_OPTION
//        );
//        return result == JOptionPane.YES_OPTION;
//    }


//    private boolean confirmBuild(){
//        int result = JOptionPane.showConfirmDialog(
//                null,
//                "Would you like to build here?",
//                "Confirm Build",
//                JOptionPane.YES_NO_OPTION
//        );
//        return result == JOptionPane.YES_OPTION;
//    }

}

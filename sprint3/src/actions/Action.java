package actions;

import boardengine.Board;
import pieces.Worker;
import playerelements.History;


/**
 * Base class for Actions. It represents the actions a worker or player can do
 *
 */
public abstract class Action {
    protected Worker worker;
    // Public

    /**
     * Constructor for Action class
     *
     * @param worker The worker that is performing the action
     */
    public Action(Worker worker) {
        this.worker = worker;
    }

    /**
     * Abstract method to return what the action does
     */
    public abstract String getStringPrompt();

    /**
     *  Abstract method to process the action on the Board class
     *
     * @param board Returns the action outcome on the board
     */
    public abstract void processAction(Board board);


    /**
     * Getter to get worker performing action
     *
     * @return Worker preforming the action
     */
    public Worker getWorker() {
        return worker;
    }

    /**
     * Processes the action and adds it to the history
     *
     * @param history History class for the action to be processed.
     */
    public void processHistory(History history) {
        history.addAction(this);
    }

    /**
     * Boolean to set a certain action as a win condtion
     *
     */
    public void setAsWinCondition(){
        winCondition = true;
    }

    /**
     * A check to see if the action is a win condition
     *
     * @return Returns a Boolean
     */
    public boolean isWinCondition(){
        return winCondition;
    }

    /**
     * Calls end turn which is a boolean
     *
     */
    public void setAsEndTurn(){
        endTurn = true;
    }

    /**
     * A boolean to determine if a certain action results in the end of a turn
     *
     * @return A boolean
     */
    public boolean isEndTurn(){
        return endTurn;
    }

    /**
     * Sets an action as Invalid
     *
     * @param invalidReason A string output on why that action is invalid
     */
    public void setAsInvalid(String invalidReason){
        validAction = false;
        validString = invalidReason;
    }

    /**
     * Sets ana action as Valid and possible
     *
     * @param ValidReason A String output on the valid action
     */
    public void setAsValid(String ValidReason){
        validAction = true;
        validString = ValidReason;
    }

    /**
     * Checks to see if the action is Valid
     *
     * @return A boolean
     */
    public boolean isValid(){
        return validAction;
    }

    /**
     * Gets the String
     *
     * @return A String
     */
    public String getValidString(){
        return validString;
    }

    // Private
    /**
     *  Sets winCondition to false
     */
    protected boolean winCondition = false;

    /**
     *  Sets endTurn to false
     */
    protected boolean endTurn = false;

    /**
     * sets validAction to true
     */
    protected boolean validAction = true;

    /**
     * Valid string output
     */
    protected String validString = "This move is valid";


}

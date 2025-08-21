package gui;



import javax.swing.*;


public class GamePanel extends JPanel {
    GameBoardPanel gameBoardPanel;
    TimerPanel timerPanel;

    public GamePanel(GameBoardPanel gameBoardPanel, TimerPanel timerPanel) {
        this.gameBoardPanel = gameBoardPanel;
        this.timerPanel = timerPanel;
        add(gameBoardPanel);
        add(timerPanel);
    }





}

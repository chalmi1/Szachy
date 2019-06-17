package Game;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * Klasa reprezentująca grę. Obsługuje dwóch graczy i odpowiada za kontrolowanie stanu gry (początek, koniec)
 */
public class Game implements WindowListener {

    public Player white = new Player(Player.Color.white);
    Player black = new Player(Player.Color.black);
    private Player turn = white;
    private int turnProgress = 0;
    private Board brd = new Board(this);
    private static Menu menu;
    private MyFrame frame;

    /**
     * Funkcja uruchamia menu
     * @param args argumenty main
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> menu = new Menu("Menu"));
    }

    /**
     * Funkcja tworzy ramkę dla gry
     */
    Game() {
        frame = new MyFrame("Szachy", brd);
        frame.addWindowListener(this);
        frame.setLocationRelativeTo(null);
    }

    Player getTurn() {
        return turn;
    }

    /**
     * funkcja powiadamia gracza że kliknięto w planszę po raz num-ty oraz na początku każdej tury sprawdza
     * czy gra została właśnie zakończona matem lub patem
     * @param num numer postępu tury, równy 1 lub 2
     */
    void notifyClick(int num) {
        assert(num == 1 || num == 2);
        if (turn == white)
            white.hasClicked();
        else
            black.hasClicked();

        turnProgress = num;
        if (turnProgress == 2) {
            switchPlayer();
            turnProgress = 0;
            int x = 100;
            if (turn.isInCheck() && brd.isCheckmate()) {
                brd.repaint();

                if (turn.getColor()== Player.Color.white) {
                    System.out.println("SZACH MAT! Czarny wygrywa!");
                    String[] options = {"OK"};
                    x = JOptionPane.showOptionDialog(null, "SZACH MAT! Czarny wygrywa!", "Mat!",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                }

                else {
                    System.out.println("SZACH MAT! Biały wygrywa!");
                    String[] options = {"OK"};
                    x = JOptionPane.showOptionDialog(null, "SZACH MAT! Biały wygrywa!", "Mat!",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                }

                brd.freeze();

            }
            if (!turn.isInCheck()) {
                brd.repaint();
                ArrayList<Move> movelist;
                if (turn.getColor() == Player.Color.white)
                    movelist = brd.generateMoveList(Tile.ColorEnum.white);
                else
                    movelist = brd.generateMoveList(Tile.ColorEnum.black);

                if (movelist.isEmpty()) {
                    System.out.println("Pat!");
                    String[] options = {"OK"};
                    x = JOptionPane.showOptionDialog(null, "Pat!", "Pat!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                brd.freeze();
                }

            }
            if (x == 0)
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    int getTurnProgress() {
        return turnProgress;
    }

    private void switchPlayer() {
        if (turn == white)
            turn = black;
        else if (turn == black)
            turn = white;
    }

    public Board getBrd() {
        return brd;
    }

    void undoClick() {
        assert(turnProgress == 1);
        if (turn == white)
            white.undoClick();
        else
            black.undoClick();

        turnProgress--;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        menu.setVisible(true);
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}

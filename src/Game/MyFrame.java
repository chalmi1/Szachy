package Game;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa odpowiadająca za okienko dla szachownicy
 */
class MyFrame extends JFrame {
    MyFrame(String name, Board board) {
        super(name);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        getContentPane().setPreferredSize(new Dimension(590, 590));
        getContentPane().add(board);
        pack();
        setResizable(false);
    }
}

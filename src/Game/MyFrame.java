package Game;

import javax.swing.*;
import java.awt.*;

class MyFrame extends JFrame {
    private MyFrame(String name) {
        super(name);
        setResizable(false);
        // Utworzenie panelu odpowiadającego za rozmiar okna (JFrame.setSize nie uwzględnia ramki)
        JPanel brd = new JPanel();
        brd.setPreferredSize(new Dimension(600, 600));
        add(brd);
        pack();
    }
    static void createAndShowGUI(Board brd) {
        MyFrame frame = new MyFrame("Szachy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(brd);
    }

}

package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Klasa odpowiadająca za okienko Menu
 */
public class Menu extends JFrame implements ActionListener {

    private JButton PvP;
    private JButton PvE;
    private JButton autor;
    private JButton exit;

    Menu(String name) {
        super(name);
        PvP = new JButton("Gracz kontra gracz");
        PvE = new JButton("Gracz kontra komputer");
        autor = new JButton("Informacje o autorze");
        exit = new JButton("Wyjście");
        showMenu();
    }

    private void showMenu() {
        setVisible(true);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(162, 255, 142));

        JPanel buttonPane = new JPanel();
        JLabel tytul = new JLabel("SZACHY", JLabel.CENTER);
        tytul.setFont(new Font("Serif", Font.BOLD, 20));
        tytul.setForeground(new Color(34, 201, 23));

        BorderLayout frameLayout = new BorderLayout();
        setLayout(frameLayout);
        frameLayout.setVgap(30);

        BoxLayout layout = new BoxLayout(buttonPane, BoxLayout.PAGE_AXIS);
        buttonPane.setLayout(layout);

        PvP.setAlignmentX(CENTER_ALIGNMENT);
        PvP.addActionListener(this);
        buttonPane.add(PvP);
        //PvE.setAlignmentX(CENTER_ALIGNMENT);
        //PvE.addActionListener(this);
        //buttonPane.add(PvE);
        autor.setAlignmentX(CENTER_ALIGNMENT);
        autor.addActionListener(this);
        buttonPane.add(autor);
        exit.setAlignmentX(CENTER_ALIGNMENT);
        exit.addActionListener(this);
        buttonPane.add(exit);
        buttonPane.setBackground(new Color(162, 255, 142));

        add(tytul, BorderLayout.PAGE_START);
        add(buttonPane, BorderLayout.CENTER);

        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == PvP) {
            new Game();
            setVisible(false);
        }
        if (source == PvE) {
            String[] options = {"Białe", "Czarne"};
            int x = JOptionPane.showOptionDialog(this, "Wybierz kolor bierek", "Wybór koloru",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        }
        if (source == autor) {
            JOptionPane.showMessageDialog(this, "Autor: Michał Chalecki. grupa I7Y1S1, Wojskowa Akademia Techniczna\n" +
                            "Program jest zadaniem projektowym" +
                    "z laboratoriów z przedmiotu Języki i Techniki Programowania prowadzonego przez dr hab. Piotra Kosiuczenko",
                    "Informacje o autorze", JOptionPane.INFORMATION_MESSAGE);

        }
        if (source == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
}

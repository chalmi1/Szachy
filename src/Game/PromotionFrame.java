package Game;

import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

public class PromotionFrame extends JFrame implements ActionListener {
    private JButton queen;
    private JButton rook;
    private JButton knight;
    private JButton bishop;
    private Tile.ColorEnum color;
    private Pawn pawn;
    private Point place;
    private Board brd;

    public PromotionFrame(String name, Pawn pawn, Board brd, Point place) {
        super(name);
        this.pawn = pawn;
        color = pawn.getColor();
        this.brd = brd;
        this.place = place;
        showGUI(color);
    }

    private void showGUI(Tile.ColorEnum color) {
        setVisible(true);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        if (color == Tile.ColorEnum.white) {
            queen = new JButton(new ImageIcon("src/img/queenw.png"));
            rook = new JButton(new ImageIcon("src/img/rookw.png"));
            knight = new JButton(new ImageIcon("src/img/knightw.png"));
            bishop = new JButton(new ImageIcon("src/img/bishopw.png"));
        }
        else {
            queen = new JButton(new ImageIcon("src/img/queenb.png"));
            rook = new JButton(new ImageIcon("src/img/rookb.png"));
            knight = new JButton(new ImageIcon("src/img/knightb.png"));
            bishop = new JButton(new ImageIcon("src/img/bishopb.png"));
        }
        queen.addActionListener(this);
        rook.addActionListener(this);
        knight.addActionListener(this);
        bishop.addActionListener(this);
        buttonPane.add(queen);
        buttonPane.add(rook);
        buttonPane.add(knight);
        buttonPane.add(bishop);
        add(buttonPane);
        pack();
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == queen) {
            brd.tile[place.y][place.x].placePiece(new Queen(color));
            brd.repaint();
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
        else if (source == rook) {
            brd.tile[place.y][place.x].placePiece(new Rook(color));
            brd.repaint();
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
        else if (source == knight) {
            brd.tile[place.y][place.x].placePiece(new Knight(color));
            brd.repaint();
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
        else if (source == bishop) {
            brd.tile[place.y][place.x].placePiece(new Bishop(color));
            brd.repaint();
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
}

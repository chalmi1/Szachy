package Game;

import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Board extends JPanel {
    public Tile[][] tile = new Tile[8][8];
    private Piece grabbedPiece = null;
    private Point firstClickCoords = null;
    private Game game;
    private Point LastMoveFrom = null;
    private Point LastMoveTo = null;

    Board(Game g) {
        game = g;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String coords = ""+((char)('a'+ col))+(8-row);
                if ((row+col)%2 == 0)
                    tile[row][col] = new Tile(coords, Tile.ColorEnum.white);
                else
                    tile[row][col] = new Tile(coords, Tile.ColorEnum.black);
            }
        }
        populate();
        ShowTextBoard();
        generateMoveList(Tile.ColorEnum.white);
        addMouseListener(new MouseAdapter() {
            Point coords;
            @Override
            public void mousePressed(MouseEvent e) {
                coords = getTileIndex(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point coords2 = getTileIndex(e.getPoint());
                if (coords.equals(coords2)) { // kursor nie opuscil pola
                    if (game.getTurnProgress() == 0 &&
                            tile[coords.y][coords.x].firstClick(game)) {
                        game.notifyClick(1);
                        grabbedPiece = tile[coords.y][coords.x].getPiece();
                        firstClickCoords = coords;
                        repaint();
                    }
                    else if (game.getTurnProgress() == 1) {
                        if (tile[coords.y][coords.x].secondClick(grabbedPiece, firstClickCoords, game.getBrd())) {
                            tile[firstClickCoords.y][firstClickCoords.x].removePiece();
                            Piece backup = tile[coords.y][coords.x].getPiece();
                            tile[coords.y][coords.x].placePiece(grabbedPiece);

                            if (updateControlledTiles() && !game.getTurn().isInCheck()) {  // jesli ruch byl dozwolony
                                tile[coords.y][coords.x].click();
                                if (LastMoveFrom != null)   // jesli byl poprzedni ruch to go podswietl
                                    tile[LastMoveFrom.y][LastMoveFrom.x].click();
                                LastMoveFrom = firstClickCoords;
                                if (LastMoveTo != null)     // jesli byl poprzedni ruch to go podswietl
                                    tile[LastMoveTo.y][LastMoveTo.x].click();
                                LastMoveTo = coords;
                                grabbedPiece = null;
                                firstClickCoords = null;
                                game.notifyClick(2);
                                resetEnPassant();
                                repaint();
                            }
                            else {
                                tile[firstClickCoords.y][firstClickCoords.x].placePiece(grabbedPiece);
                                tile[coords.y][coords.x].placePiece(backup);
                            }

                        }
                        else {
                            tile[firstClickCoords.y][firstClickCoords.x].click();
                            grabbedPiece = null;
                            firstClickCoords = null;
                            game.undoClick();
                            repaint();
                        }
                    }
                }
            }
        });
    }

    private boolean updateControlledTiles() {   // funkcja aktualizująca flagi pól kontrolowanych
        // funkcja zwraca true lub false w zależności od tego czy wykonany wcześniej ruch był prawidłowy
        //  pod kątem pól kontrolowanych
        // flagi są używane aby
        // 1. uniemożliwiać ruch króla w zaszachowane pole
        // 2. uniemożliwić ruch bierki przypiętej absolutnie
        //  (tak że po jej ruchu odsłonięty zostałby szach na własnym królu)
        // 3. wymusić wyjście z szacha wykonanego przez przeciwnika
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                tile[row][col].resetControlled();
            }
        }
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Piece piece = tile[row][col].getPiece();
                if (piece == null)
                    continue;
                piece.updateControlled(new Point(row, col), this);  // każda bierka ustawia flagi kontroli
                // na polach które obecnie kontroluje
            }
        }
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Piece piece = tile[row][col].getPiece();
                if (piece == null)
                    continue;
                if (piece.getSymbol() == 'k' || piece.getSymbol() == 'K') {
                    switch (game.getTurn().getColor()) {
                    case white: // czy po ruchu białego biały jest szachowany?
                        if (piece.getColor() == Tile.ColorEnum.white) {
                            // król jest szachowany
                            if (tile[row][col].getControlled(Tile.ColorEnum.black))
                                return false;   // (ruch niedozwolony)
                            else
                                game.white.setInCheck(false);
                        }
                        break;
                    case black: // czy po ruchu czarnego czarny jest szachowany?
                        if (piece.getColor() == Tile.ColorEnum.black) {
                            // król jest szachowany
                            if (tile[row][col].getControlled(Tile.ColorEnum.white))
                                return false;   // (ruch niedozwolony)
                            else
                                game.black.setInCheck(false);

                        }
                        break;
                    }
                }
            }
        }
        for (int col = 0; col < 8; col++) { // pętla aktualizująca flagi szacha
            for (int row = 0; row < 8; row++) {
                Piece piece = tile[row][col].getPiece();
                if (piece == null)
                    continue;

                if (piece.getSymbol() == 'k' || piece.getSymbol() == 'K') {
                    switch (game.getTurn().getColor()) {
                        case white: // czy po ruchu białego czarny jest szachowany?
                            if (piece.getColor() == Tile.ColorEnum.black) {
                                // król jest szachowany
                                if (tile[row][col].getControlled(Tile.ColorEnum.white)) {
                                    game.black.setInCheck(true);
                                    System.out.println("Szach!");
                                }

                                else
                                    game.black.setInCheck(false);
                            }
                            break;
                        case black: // czy po ruchu czarnego biały jest szachowany?
                            if (piece.getColor() == Tile.ColorEnum.white) {
                                // król jest szachowany
                                if (tile[row][col].getControlled(Tile.ColorEnum.black)) {
                                    game.white.setInCheck(true);
                                    System.out.println("Szach!");
                                }

                                else
                                    game.white.setInCheck(false);

                            }
                            break;
                    }
                }
            }
        }

        return true;
    }

    private void resetEnPassant() { // usuwa możliwość bicia w przelocie
            // (funkcja powinna być wywoływana po każdej turze)
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Piece piece = tile[row][col].getPiece();
                if (piece == null)
                    continue;
                if (piece.getSymbol() == 'p' || piece.getSymbol() == 'P') {
                    if ((game.getTurn().getColor() == Player.Color.white &&
                            piece.getColor() == Tile.ColorEnum.white) ||
                            (game.getTurn().getColor() == Player.Color.black &&
                                    piece.getColor() == Tile.ColorEnum.black)) {
                        Pawn pwn = (Pawn) piece;
                        pwn.resetEnPassant();   // anulowanie mozliwosci bicia w przelocie po ruchu
                    }
                }
            }
        }
    }

    public void paint(Graphics g) {
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                tile[row][col].draw(g);
            }
        }
    }

    private void populate() {
        // Wieże
        tile[0][0].placePiece(new Rook(Tile.ColorEnum.black));
        tile[0][7].placePiece(new Rook(Tile.ColorEnum.black));
        tile[7][0].placePiece(new Rook(Tile.ColorEnum.white));
        tile[7][7].placePiece(new Rook(Tile.ColorEnum.white));
        // Skoczki
        tile[0][1].placePiece(new Knight(Tile.ColorEnum.black));
        tile[0][6].placePiece(new Knight(Tile.ColorEnum.black));
        tile[7][1].placePiece(new Knight(Tile.ColorEnum.white));
        tile[7][6].placePiece(new Knight(Tile.ColorEnum.white));
        // Gońce
        tile[0][2].placePiece(new Bishop(Tile.ColorEnum.black));
        tile[0][5].placePiece(new Bishop(Tile.ColorEnum.black));
        tile[7][2].placePiece(new Bishop(Tile.ColorEnum.white));
        tile[7][5].placePiece(new Bishop(Tile.ColorEnum.white));
        // Hetmany
        tile[0][3].placePiece(new Queen(Tile.ColorEnum.black));
        tile[7][3].placePiece(new Queen(Tile.ColorEnum.white));
        // Królowie
        tile[0][4].placePiece(new King(Tile.ColorEnum.black));
        tile[7][4].placePiece(new King(Tile.ColorEnum.white));
        // Pionki
        tile[1][0].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][1].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][2].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][3].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][4].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][5].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][6].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[1][7].placePiece(new Pawn(Tile.ColorEnum.black));
        tile[6][0].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][1].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][2].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][3].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][4].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][5].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][6].placePiece(new Pawn(Tile.ColorEnum.white));
        tile[6][7].placePiece(new Pawn(Tile.ColorEnum.white));
    }

    private void ShowTextBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (tile[row][col].getPiece() != null)
                System.out.print(tile[row][col].getPiece().getSymbol()+" ");
                else
                    System.out.print("- ");
            }
            System.out.print("\n");
        }
    }

    private Point getTileIndex(Point p) {
        return new Point(p.x/Tile.dimension, p.y/Tile.dimension);
    }

    public boolean isInside(Point p) {
        return p.x <= 7 && p.x >= 0 && p.y >= 0 && p.y <= 7;
    }


    private ArrayList<Move> generateMoveList(Tile.ColorEnum color) {
        // TODO: listuj tylko te kolorki
        ArrayList<Move> list = new ArrayList<Move>();
        for (int col = 0; col < 8; col++) { // pętla zbierająca wszystkie dozwolone ruchy bierek
            for (int row = 0; row < 8; row++) {
                Piece piece = tile[row][col].getPiece();
                if (piece == null)
                    continue;

                for (int x = 0; x < 8; x++) { // pętla zbierająca wszystkie dozwolone ruchy bierek
                    for (int y = 0; y < 8; y++) {
                        Move move = new Move(new Point(col, row), new Point(x,y));
                        if (piece.isLegal(move.from, move.to, this)) {
                            list.add(move);
                            System.out.println("Ruch: "+move.from.x+move.from.y+"-"+move.to.x+move.to.y);
                        }
                    }
                }
            }
        }
        return list;
    }

    private boolean isCheckmate() {
        Tile.ColorEnum color;
        switch(game.getTurn().getColor())
        {
            case white:
                color = Tile.ColorEnum.white;
                break;
            case black:
                color = Tile.ColorEnum.black;
                break;
            default:
                color = Tile.ColorEnum.white;
        }
        ArrayList<Move> list = generateMoveList(color);
        for ( Move i : list) {  // pętla po liście ruchów
            // wykonanie ruchu
            Piece grabbedPiece = tile[i.from.y][i.from.x].getPiece();
            tile[i.from.y][i.from.x].removePiece();
            Piece backup = tile[i.to.y][i.to.x].getPiece();
            tile[i.to.y][i.to.x].placePiece(grabbedPiece);

            updateControlledTiles();

            // przywracanie poprzedniego stanu
            tile[i.from.y][i.from.x].placePiece(grabbedPiece);
            tile[i.to.y][i.to.x].removePiece();
            if (backup != null)
                tile[i.to.y][i.to.x].placePiece(backup);

            if (!game.getTurn().isInCheck())
            {
                updateControlledTiles();
                return false;
            }

        }
        updateControlledTiles();
        return true;
    }


}

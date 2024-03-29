package Game;

import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Klasa reprezentująca szachownicę. Składa się z tablicy płytek (obiektów typu Tile)
 */
public class Board extends JPanel {
    public Tile[][] tile = new Tile[8][8];
    private Piece grabbedPiece = null;
    private Point firstClickCoords = null;
    private Game game;
    private Point LastMoveFrom = null;
    private Point LastMoveTo = null;
    private MouseAdapter mouse;

    /**
     * Konstruktor inicjalizuje planszę:
     * 1. inicjalizuje wszystkie pola planszy (obiekty typu Tile) przypisując im koordynaty i kolor
     * 2. ustawia ustawienie początkowe do gry w szachy (bierki na odpowiednich pozycjach)
     * 3. definiuje słuchacza obsługującą kolejne etapy wykonania ruchu: kliknięcie na bierkę, kliknięcie na pole docelowe
     * @param g Obiekt klasy Game reprezentujący grę która się na tej szachownicy będzie odbywać
     *
     */
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
        mouse = new MouseAdapter() {
            Point coords;
            @Override
            public void mousePressed(MouseEvent e) {
                coords = getTileIndex(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point coords2 = getTileIndex(e.getPoint());
                int button = e.getButton();
                if (button == 1) {
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
                                Piece backup = null;
                                if (tile[coords.y][coords.x].isOccupied())
                                    backup = tile[coords.y][coords.x].getPiece();
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
                                    if (backup != null)
                                        tile[coords.y][coords.x].placePiece(backup);
                                    else
                                        tile[coords.y][coords.x].removePiece();
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
                /*else if (button == 3) {
                    Piece pc;
                    if (coords.equals(coords2)) { // kursor nie opuscil pola
                        if (tile[coords.y][coords.x].isOccupied()) {
                            pc = tile[coords.y][coords.x].getPiece();
                            ArrayList<Move> list = generateMoveList(pc);
                            for (Move i : list) {
                                if (!tile[i.to.y][i.to.x].dotted)
                                tile[i.to.y][i.to.x].dotted = true;
                                else tile[i.to.y][i.to.x].dotted = false;

                            }
                            repaint();
                        }
                    }
                }*/

            }
        };
        addMouseListener(mouse);
    }

    /**
     * funkcja aktualizująca flagi pól kontrolowanych
     * funkcja zwraca true lub false w zależności od tego czy wykonany wcześniej ruch był prawidłowy
     *  pod kątem pól kontrolowanych
     * flagi są używane aby
     * 1. uniemożliwiać ruch króla w zaszachowane pole
     * 2. uniemożliwić ruch bierki przypiętej absolutnie
     *  (tak że po jej ruchu odsłonięty zostałby szach na własnym królu)
     * 3. wymusić wyjście z szacha wykonanego przez przeciwnika
     * @return true lub false w zależności od tego czy wykonany wcześniej ruch był prawidłowy pod kątem ewentualnego
     * odkrywania szacha lub nie zareagowania na szach przeciwnika
     */
    private boolean updateControlledTiles() {
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                tile[row][col].resetControlled();
            }
        }
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Piece piece;
                if (tile[row][col].isOccupied())
                piece = tile[row][col].getPiece();
                else
                    continue;
                piece.updateControlled(new Point(row, col), this);  // każda bierka ustawia flagi kontroli
                // na polach które obecnie kontroluje
            }
        }
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Piece piece;
                if (tile[row][col].isOccupied())
                    piece = tile[row][col].getPiece();
                else
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
                Piece piece;
                if (tile[row][col].isOccupied())
                    piece = tile[row][col].getPiece();
                else
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

    /**
     * usuwa możliwość bicia w przelocie
     * (funkcja powinna być wywoływana po każdej turze)
     */
    private void resetEnPassant() {
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Piece piece;
                if (tile[row][col].isOccupied())
                    piece = tile[row][col].getPiece();
                else
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

    /**
     * funkcja rysująca planszę (przesłonięta funkcja z klasy JComponent)
     * @param g obiekt Graphics
     */
    public void paint(Graphics g) {
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                tile[row][col].draw(g);

            }
        }
    }

    /**
     * funkcja ustawiająca początkowe ustawienie bierek do gry w szachy
     */
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

    /**
     * funkcja rysująca szachownicę w formie tekstowej do konsoli
     */
    private void ShowTextBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (tile[row][col].isOccupied())
                System.out.print(tile[row][col].getPiece().getSymbol()+" ");
                else
                    System.out.print("- ");
            }
            System.out.print("\n");
        }
    }

    /**
     * @param p współrzędne punktu kliknięcia na szachownicy w pikselach
     * @return współrzędne punktu kliknięcia na szachownicy w indeksach tablicy płytek
     */
    private Point getTileIndex(Point p) {
        Point index = new Point(p.x/Tile.dimension, p.y/Tile.dimension);
        if (index.x > 7)
            index.x = 7;
        if (index.y > 7)
            index.y = 7;

        return index;
    }

    /**
     * @param p punkt (we współrzędnych indeksowanych) na szachownicy
     * @return true gdy punkt znajduje się wewnątrz szachownicy
     */
    public boolean isInside(Point p) {
        return p.x <= 7 && p.x >= 0 && p.y >= 0 && p.y <= 7;
    }


    /**
     * Funkcja generująca listę dozwolonych ruchów wszystkich bierek danego koloru (bez patrzenia na ewentualne
     * odkrywanie szacha)
     * @param color kolor bierek
     * @return lista dozwolonych ruchów
     */
    ArrayList<Move> generateMoveList(Tile.ColorEnum color) {
        ArrayList<Move> list = new ArrayList<Move>();
        for (int col = 0; col < 8; col++) { // pętla zbierająca wszystkie dozwolone ruchy bierek
            for (int row = 0; row < 8; row++) {
                Piece piece;
                if (tile[row][col].isOccupied())
                    piece = tile[row][col].getPiece();
                else
                    continue;
                if (piece.getColor() != color)
                    continue;

                for (int x = 0; x < 8; x++) { // pętla zbierająca wszystkie dozwolone ruchy bierek
                    for (int y = 0; y < 8; y++) {
                        Move move = new Move(new Point(col, row), new Point(x,y));
                        if (piece.isLegal(move.from, move.to, this)) {
                            list.add(move);
                        }
                    }
                }
            }
        }
        return list;
    }

    ArrayList<Move> generateMoveList(Piece pc) {
        ArrayList<Move> list = new ArrayList<Move>();
        for (int col = 0; col < 8; col++) { // pętla zbierająca wszystkie dozwolone ruchy bierek
            for (int row = 0; row < 8; row++) {
                Piece piece;
                if (tile[row][col].isOccupied()) {
                    piece = tile[row][col].getPiece();
                    if (piece != pc)
                        continue;
                }
                else
                    continue;

                for (int x = 0; x < 8; x++) { // pętla zbierająca wszystkie dozwolone ruchy bierek
                    for (int y = 0; y < 8; y++) {
                        Move move = new Move(new Point(col, row), new Point(x,y));
                        if (piece.isLegal(move.from, move.to, this)) {
                            list.add(move);
                        }
                    }
                }
            }
        }
        return list;
    }



    /**
     * @return true gdy jest szach-mat na graczu którego jest obecnie tura
     */
    boolean isCheckmate() {
        updateControlledTiles();
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
        if (!game.getTurn().isInCheck())
            return false;
        ArrayList<Move> list = generateMoveList(color);
        for ( Move i : list) {  // pętla po liście ruchów
            // wykonanie ruchu
            Piece grabbedPiece = tile[i.from.y][i.from.x].getPiece();
            tile[i.from.y][i.from.x].removePiece();
            Piece backup = null;
            if (tile[i.to.y][i.to.x].isOccupied())
                backup = tile[i.to.y][i.to.x].getPiece();
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

    /**
     * funkcja zamrażająca planszę, czyli powodująca że plansza nie będzie reagować na kliknięcia myszki
     */
    void freeze() {
        removeMouseListener(mouse);
    }

    /**
     * funkcja anulująca zamrożenie planszy
     */
    void unfreeze() {
        addMouseListener(mouse);
    }
}

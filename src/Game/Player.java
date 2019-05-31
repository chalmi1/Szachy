package Game;

class Player {


    public enum Color {white, black}
    private Color color;
    private int clicked = 0;    // ilosc klikniec: 0 - nic sie nie stalo
                                // 1 - naciśnięta bierka do ruszenia
                                // 2 - naciśnięte pole docelowe
    private boolean inCheck = false;

    Player(Color c) {
        color = c;
    }

    Color getColor() {return color;}

    void hasClicked() {
        clicked++;
    }

    void undoClick() { clicked--; }

    boolean isInCheck() {
        return inCheck;
    }

    void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }
}

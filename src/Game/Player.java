package Game;

public class Player {


    public enum Color {white, black}
    private Color color;
    private int clicked = 0;    // ilosc klikniec: 0 - nic sie nie stalo
                                // 1 - naciśnięta bierka do ruszenia
                                // 2 - naciśnięte pole docelowe

    Player(Color c) {
        color = c;
    }

    Color getColor() {return color;}

    void hasClicked() {
        clicked++;
    }
}

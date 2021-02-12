public class Laser {
    private int x;
    private int y;
    private char symbol;
    private boolean isActive;

    public Laser(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    public Laser() {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}



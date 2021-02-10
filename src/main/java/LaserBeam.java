public class LaserBeam {
    private int x;
    private int y;
    private int oldX;
    private int oldY;
    private char symbol;

    public LaserBeam(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.oldX = oldX;
        this.oldY = oldY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public char getSymbol() {
        return symbol;
    }

    public void moveUp() {
        oldX = x;
        oldY = y;
        y -= 4;
    }
}

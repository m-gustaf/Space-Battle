public class Player {
    private int x;
    private int y;
    private int oldX;
    private int oldY;
    private char symbol;

    public Player(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.oldY = oldY;
        this.oldX = oldX;
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

    public void moveLeft(){
        oldX = x;
        oldY = y;
        x -= 2;
    }

    public void moveRight(){
        oldX = x;
        oldY = y;
        x += 2;
    }
}

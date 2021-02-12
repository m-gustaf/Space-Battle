public class Spaceship extends Character {

    public Spaceship(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.oldX = oldX;
        this.oldY = oldY;
    }

    public Spaceship() {
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

    public void setY(int y) {
        this.y = y-1;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public void moveDown() {
        oldX = x;
        oldY = y;
        y += 1;
    }
}

public class Difficulty {
    private int level;
    private int gameSpeed;

    public Difficulty(int level, int gameSpeed) {
        this.level = level;
        this.gameSpeed = gameSpeed;
    }

    public int getLevel() {
        return level;
    }

    public int getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(int gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    public void increaseLevel() {
        level++;
    }

    public void increaseGameSpeed() {
        gameSpeed -= 12;
    }
}

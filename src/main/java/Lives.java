public class Lives {

    private int lives;

    public Lives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public void updateLives() {
        lives--;
    }
}

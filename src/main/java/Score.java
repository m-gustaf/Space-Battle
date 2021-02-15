public class Score {

    private int playerScore;
    private int alienScore;

    public int getPlayerScore() {
        return playerScore;
    }
    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }
    public void updatePlayerScore() {
        playerScore++;
    }
    public int getAlienScore() {
        return alienScore;
    }
    public void setAlienScore(int alienScore) {
        this.alienScore = alienScore;
    }
    public void updateAlienScore() {
        alienScore++;
    }
}
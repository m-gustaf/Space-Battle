public class Score {
    private int playerScore;
    private int opponentScore;

    public int getPlayerScore() {
        return playerScore;
    }

    public void updatePlayerScore() {
        playerScore++;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public void updateOpponentScore() {
        opponentScore++;
    }
}
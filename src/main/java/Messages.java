import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;

public class Messages {

    public void gameTitle(Terminal terminal) throws InterruptedException, IOException {
        String title = "SPACE BATTLE";
        for (int i = 0; i < title.length(); i++) {
            terminal.setCursorPosition(i + 2, 1);
            terminal.putCharacter(title.charAt(i));
        }
    }

    public void level(Terminal terminal, Difficulty difficulty) throws InterruptedException, IOException {
        String level = "Level: " + difficulty.getLevel();
        for (int i = 0; i < level.length(); i++) {
            terminal.setCursorPosition(i + 2, 3);
            terminal.putCharacter(level.charAt(i));
            terminal.flush();
        }
    }

    public void playerScore(Terminal terminal, Score score) throws InterruptedException, IOException {
        String displayPlayerScore = "Score: " + score.getPlayerScore();
        for (int i = 0; i < displayPlayerScore.length(); i++) {
            terminal.setCursorPosition(i + 2, 5);
            terminal.putCharacter(displayPlayerScore.charAt(i));
            terminal.flush();
        }
    }

    public void lives (Terminal terminal, Lives lives) throws InterruptedException, IOException {
        String live = "Lives: " + lives.getLives();
        for (int i = 0; i < live.length(); i++) {
            terminal.setCursorPosition(i + 2, 6);
            terminal.putCharacter(live.charAt(i));
            terminal.flush();
        }
    }

    public void objective (Terminal terminal) throws InterruptedException, IOException {
        String objective = "Eliminate 30 aliens!";
        for (int i = 0; i < objective.length(); i++) {
            terminal.setCursorPosition(i + 28, 9);
            terminal.putCharacter(objective.charAt(i));
            terminal.flush();
        }
    }

    public void levelUpMessage (Terminal terminal) throws InterruptedException, IOException {
        String levelUp = "Level Up!";
        for (int i = 0; i < levelUp.length(); i++) {
            terminal.setCursorPosition(i + 34, 9);
            terminal.putCharacter(levelUp.charAt(i));
            terminal.flush();
        }
    }

    public void gameOver(Terminal terminal, Score score, Lives lives) throws InterruptedException, IOException {
        String gameOver = "GAME OVER";
        String playerWon = "You beat the game!";
        String playerLost = "You lost, try again!";

        for (int i = 0; i < gameOver.length(); i++) {
            terminal.setCursorPosition(i + 28, 9);
            terminal.putCharacter(gameOver.charAt(i));
            terminal.flush();
            Thread.sleep(200); //sleep to get effect of slowly printing GAME OVER
        }

        if (lives.getLives() > 0) {
            for (int i = 0; i < playerWon.length(); i++) {
                terminal.setCursorPosition(i + 28, 11);
                terminal.putCharacter(playerWon.charAt(i));
                terminal.flush();
            }
        }
        if (lives.getLives() == 0) {
            for (int i = 0; i < playerLost.length(); i++) {
                terminal.setCursorPosition(i + 28, 11);
                terminal.putCharacter(playerLost.charAt(i));
                terminal.flush();
            }
        }
    }

    public void hideMessages (Terminal terminal) throws InterruptedException, IOException {
        String hideMessages = "                                        ";
        for (int i = 0; i < hideMessages.length(); i++) {
            terminal.setCursorPosition(i + 20, 9);
            terminal.putCharacter(hideMessages.charAt(i));
            terminal.flush();
        }
    }

}

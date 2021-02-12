import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SpaceBattle {

    public static void main(String[] args) throws Exception {

        Terminal terminal = createTerminal();

        Score score = new Score();

        Spaceship spaceship = createSpaceship();
        Player player = createPlayer();

        drawPlayer(terminal, player);
        drawSpaceship(terminal, spaceship);

        do {
            gameTitle(terminal);
            playerScore(terminal, score);
            opponentScore(terminal, score);

            if (spaceship.isHidden() == true) { //enemy isHidden = true id player and enemy is at same Y value.
                spaceship = createSpaceship();
            }

            KeyStroke keyStroke = getUserKeyStroke(terminal, player, spaceship, score);

            movePlayer(player, spaceship, score, terminal, keyStroke);
            drawPlayer(terminal, player);

            moveSpaceship(spaceship);
            drawSpaceship(terminal, spaceship);

            if (score.getPlayerScore() == 5 || score.getOpponentScore() == 5) {
                break;
            }

        } while (true);

        gameOver(terminal, score);
        playerScore(terminal, score);
        opponentScore(terminal, score);
    }

    private static Terminal createTerminal() throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
        return terminal;
    }

    private static Player createPlayer() {
        return new Player(39, 23, 'A');
    }

    private static Spaceship createSpaceship() {
        int randomX = ThreadLocalRandom.current().nextInt(0, 80);
        int randomShip = ThreadLocalRandom.current().nextInt(0, 3);
        ArrayList<Spaceship> listOfSpaceships = new ArrayList<>();
        listOfSpaceships.add(new Spaceship(randomX, 0, '¥'));
        listOfSpaceships.add(new Spaceship(randomX, 0, 'Ÿ'));
        listOfSpaceships.add(new Spaceship(randomX, 0, 'v'));
        listOfSpaceships.add(new Spaceship(randomX, 0, 'Ü'));
        return listOfSpaceships.get(randomShip);
    }

    private static void drawPlayer(Terminal terminal, Player player) throws IOException {
        terminal.setCursorPosition(player.getOldX(), player.getOldY());
        terminal.putCharacter(' ');

        terminal.setCursorPosition(player.getX(), player.getY());
        terminal.putCharacter(player.getSymbol());

        terminal.flush();
    }

    private static void drawSpaceship(Terminal terminal, Spaceship spaceship) throws IOException {
        terminal.setCursorPosition(spaceship.getOldX(), spaceship.getOldY());
        terminal.putCharacter(' ');

        terminal.setCursorPosition(spaceship.getX(), spaceship.getY());
        terminal.putCharacter(spaceship.getSymbol());

        terminal.flush();
    }

    private static void movePlayer(Player player, Spaceship spaceship, Score score, Terminal terminal, KeyStroke keyStroke) throws IOException {
        switch (keyStroke.getKeyType()) {
            case ArrowLeft:
                player.moveLeft();
                spaceship.setY(spaceship.getY());
                break;
            case ArrowRight:
                player.moveRight();
                spaceship.setY(spaceship.getY());
                break;
            case ArrowDown:
                terminal.bell(); //dropping bomb
                if (spaceship.getX() == player.getX() || spaceship.getX() + 1 == player.getX() || spaceship.getX() - 1 == player.getX()) {
                    spaceship.setSymbol(' ');
                    spaceship.setIsHidden(true);
                    score.updatePlayerScore();
                    terminal.flush();
                    break;
                }
        }
    }

    private static void moveSpaceship(Spaceship spaceship) {
        spaceship.moveDown();
    }

    private static KeyStroke getUserKeyStroke(Terminal terminal, Player player, Spaceship spaceship, Score score) throws InterruptedException, IOException {
        KeyStroke keyStroke;
        int index = 0;
        if (spaceship.getSymbol() == 'Ÿ') { //to have one type of ship move faster
            do {
                Thread.sleep(5);
                if (index % 20 == 0) {
                    moveSpaceship(spaceship);
                    drawSpaceship(terminal, spaceship);
                    if (player.getY() == spaceship.getY()) {
                        spaceship.setSymbol(' ');
                        spaceship.setIsHidden(true);
                        score.updateOpponentScore();
                    }
                }
                index++;
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);
        } else {
            do {
                Thread.sleep(5);
                if (index % 50 == 0) {
                    moveSpaceship(spaceship);
                    drawSpaceship(terminal, spaceship);
                    if (player.getY() == spaceship.getY()) {
                        spaceship.setSymbol(' ');
                        spaceship.setIsHidden(true);
                        score.updateOpponentScore();
                    }
                }
                index++;
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);
        }
        return keyStroke;
    }

    private static void gameTitle(Terminal terminal) throws InterruptedException, IOException {
        String title = "SPACE BATTLE";
        for (int i = 0; i < title.length(); i++) {
            terminal.setCursorPosition(i + 2, 1);
            terminal.putCharacter(title.charAt(i));
        }
    }

    private static void playerScore(Terminal terminal, Score score) throws InterruptedException, IOException {
        String displayPlayerScore = "Player score: " + score.getPlayerScore();
        for (int i = 0; i < displayPlayerScore.length(); i++) {
            terminal.setCursorPosition(i + 2, 3);
            terminal.putCharacter(displayPlayerScore.charAt(i));
            terminal.flush();
        }
    }

    private static void opponentScore(Terminal terminal, Score score) throws InterruptedException, IOException {
        String displayOpponentScore = "Opponent score: " + score.getOpponentScore();
        for (int i = 0; i < displayOpponentScore.length(); i++) {
            terminal.setCursorPosition(i + 2, 4);
            terminal.putCharacter(displayOpponentScore.charAt(i));
            terminal.flush();
        }
    }

    private static void gameOver(Terminal terminal, Score score) throws InterruptedException, IOException {
        String playerWon = "Congratulations, you won!";
        String opponentWon = "Bad luck, you lost!";
        String gameOver = "GAME OVER";

        for (int i = 0; i < gameOver.length(); i++) {
            terminal.setCursorPosition(i + 10, 12);
            terminal.putCharacter(gameOver.charAt(i));
            terminal.flush();
        }

        if (score.getPlayerScore() > score.getOpponentScore()) {
            for (int i = 0; i < playerWon.length(); i++) {
                terminal.setCursorPosition(i + 10, 10);
                terminal.putCharacter(playerWon.charAt(i));
                terminal.flush();
            }
        }
        if (score.getOpponentScore() > score.getPlayerScore()) {
            for (int i = 0; i < opponentWon.length(); i++) {
                terminal.setCursorPosition(i + 10, 10);
                terminal.putCharacter(opponentWon.charAt(i));
                terminal.flush();
            }
        }
    }
}


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

        Alien alien = createSpaceship();
        Player player = createPlayer();

        drawPlayer(terminal, player);
        drawSpaceship(terminal, alien);

        do {
            gameTitle(terminal);
            playerScore(terminal, score);
            alienScore(terminal, score);

            if (alien.isHidden() == true) { //enemy isHidden = true if player and enemy is at same Y value.
                alien = createSpaceship();
            }

            KeyStroke keyStroke = getUserKeyStroke(terminal, player, alien, score);

            movePlayer(player, alien, score, terminal, keyStroke);
            drawPlayer(terminal, player);

            moveSpaceship(alien);
            drawSpaceship(terminal, alien);

        } while (score.getPlayerScore() < 2 && score.getAlienScore() < 2);

        gameOver(terminal, score);
        playerScore(terminal, score);
        alienScore(terminal, score);
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

    private static Alien createSpaceship() {
        int randomX = ThreadLocalRandom.current().nextInt(0, 80);
        int randomShip = ThreadLocalRandom.current().nextInt(0, 3);
        ArrayList<Alien> listOfAliens = new ArrayList<>();
        listOfAliens.add(new Alien(randomX, 0, '¥'));
        listOfAliens.add(new Alien(randomX, 0, 'Ÿ'));
        listOfAliens.add(new Alien(randomX, 0, 'v'));
        listOfAliens.add(new Alien(randomX, 0, 'Ü'));
        return listOfAliens.get(randomShip);
    }

    private static void drawPlayer(Terminal terminal, Player player) throws IOException {
        terminal.setCursorPosition(player.getOldX(), player.getOldY());
        terminal.putCharacter(' ');

        terminal.setCursorPosition(player.getX(), player.getY());
        terminal.putCharacter(player.getSymbol());

        terminal.flush();
    }

    private static void drawSpaceship(Terminal terminal, Alien alien) throws IOException {
        terminal.setCursorPosition(alien.getOldX(), alien.getOldY());
        terminal.putCharacter(' ');

        terminal.setCursorPosition(alien.getX(), alien.getY());
        terminal.putCharacter(alien.getSymbol());

        terminal.flush();
    }

    private static void movePlayer(Player player, Alien alien, Score score, Terminal terminal, KeyStroke keyStroke) throws IOException {
        switch (keyStroke.getKeyType()) {
            case ArrowLeft:
                player.moveLeft();
                alien.setY(alien.getY());
                break;
            case ArrowRight:
                player.moveRight();
                alien.setY(alien.getY());
                break;
            case ArrowDown:
                terminal.bell(); //dropping bomb
                if (alien.getX() == player.getX() || alien.getX() + 1 == player.getX() || alien.getX() - 1 == player.getX()) {
                    alien.setSymbol(' ');
                    alien.setIsHidden(true);
                    score.updatePlayerScore();
                    terminal.flush();
                    break;
                }
        }
    }

    private static void moveSpaceship(Alien alien) {
        alien.moveDown();
    }

    private static KeyStroke getUserKeyStroke(Terminal terminal, Player player, Alien alien, Score score) throws InterruptedException, IOException {
        KeyStroke keyStroke;
        int index = 0;
        if (alien.getSymbol() == 'Ÿ') { //to have one type of ship move faster
            do {
                Thread.sleep(5);
                if (index % 20 == 0) {
                    moveSpaceship(alien);
                    drawSpaceship(terminal, alien);
                    if (player.getY() == alien.getY()) {
                        alien.setSymbol(' ');
                        alien.setIsHidden(true);
                        score.updateAlienScore();
                    }
                }
                index++;
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);
        } else {
            do {
                Thread.sleep(5);
                if (index % 50 == 0) {
                    moveSpaceship(alien);
                    drawSpaceship(terminal, alien);
                    if (player.getY() == alien.getY()) {
                        alien.setSymbol(' ');
                        alien.setIsHidden(true);
                        score.updateAlienScore();
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

    private static void alienScore(Terminal terminal, Score score) throws InterruptedException, IOException {
        String displayAlienScore = "Alien score: " + score.getAlienScore();
        for (int i = 0; i < displayAlienScore.length(); i++) {
            terminal.setCursorPosition(i + 2, 4);
            terminal.putCharacter(displayAlienScore.charAt(i));
            terminal.flush();
        }
    }

    private static void gameOver(Terminal terminal, Score score) throws InterruptedException, IOException {
        String gameOver = "GAME OVER";
        String playerWon = "Congratulations, you won!";
        String opponentWon = "Bad luck, you lost!";

        for (int i = 0; i < gameOver.length(); i++) {
            terminal.setCursorPosition(i + 25, 8);
            terminal.putCharacter(gameOver.charAt(i));
            terminal.flush();
        }

        if (score.getPlayerScore() > score.getAlienScore()) {
            for (int i = 0; i < playerWon.length(); i++) {
                terminal.setCursorPosition(i + 25, 10);
                terminal.putCharacter(playerWon.charAt(i));
                terminal.flush();
            }
        }
        if (score.getAlienScore() > score.getPlayerScore()) {
            for (int i = 0; i < opponentWon.length(); i++) {
                terminal.setCursorPosition(i + 25, 10);
                terminal.putCharacter(opponentWon.charAt(i));
                terminal.flush();
            }
        }
    }
}


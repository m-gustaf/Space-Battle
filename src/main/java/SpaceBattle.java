import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SpaceBattle {

    public static void main(String[] args) throws Exception {

        Terminal terminal = createTerminal();

        Spaceship spaceship = new Spaceship();
        spaceship = createSpaceship();
        Score score = new Score();

        Player player = createPlayer();
        drawPlayer(terminal, player);
        drawSpaceship(terminal, spaceship);

        do {
            gameTitle(terminal);
            gameScore(terminal, score);

            if (spaceship.getSymbol() == ' ') {
                spaceship = createSpaceship();
            }

            KeyStroke keyStroke = getUserKeyStroke(terminal, spaceship);

            movePlayer(player, spaceship, score, terminal, keyStroke);
            drawPlayer(terminal, player);

            moveSpaceship(spaceship);
            drawSpaceship(terminal, spaceship);

        } while (true);
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
        int randomX = ThreadLocalRandom.current().nextInt(0,80);
        int randomShip = ThreadLocalRandom.current().nextInt(0,3);
        ArrayList<Spaceship> listOfSpaceships = new ArrayList<>();
        listOfSpaceships.add(new Spaceship(randomX,0,'¥'));
        listOfSpaceships.add(new Spaceship(randomX,0,'Ÿ'));
        listOfSpaceships.add(new Spaceship(randomX,0,'v'));
        listOfSpaceships.add(new Spaceship(randomX,0,'Ü'));
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
                spaceship.setSymbol(' ');
                terminal.flush();
                score.updateScore();
                break;
            case ArrowUp:
                break;
        }
    }

    private static void moveSpaceship(Spaceship spaceship) {
        spaceship.moveDown();
    }

    private static KeyStroke getUserKeyStroke(Terminal terminal, Spaceship spaceship) throws InterruptedException, IOException {
        KeyStroke keyStroke;
        int index = 0;
        do {
            Thread.sleep(5);
            if (index % 50 == 0) {
                moveSpaceship(spaceship);
                drawSpaceship(terminal, spaceship);
            }
            index++;
            keyStroke = terminal.pollInput();
        } while (keyStroke == null);
        return keyStroke;
    }

    private static void gameTitle(Terminal terminal) throws InterruptedException, IOException {
        String title = "SPACE BATTLE";
        for (int i = 0; i < title.length(); i++) {
            terminal.setCursorPosition(i+2,1);
            terminal.putCharacter(title.charAt(i));
        }
    }

    private static void gameScore(Terminal terminal, Score score) throws InterruptedException, IOException {
        String displayScore = "Score: " + score.getScore();
        for (int i = 0; i < displayScore.length(); i++) {
            terminal.setCursorPosition(i+2,3);
            terminal.putCharacter(displayScore.charAt(i));
        }

    }
}

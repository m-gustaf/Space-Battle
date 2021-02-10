import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class SpaceBattle {

    public static void main(String[] args) throws Exception {
        Terminal terminal = createTerminal();

        Player player = createPlayer();
        drawPlayer(terminal, player);

        Spaceship spaceship = createSpaceship();
        drawSpaceships(terminal, spaceship);

        do {
            KeyStroke keyStroke = getUserKeyStroke(terminal, spaceship);

            movePlayer(player, keyStroke);
            drawPlayer(terminal, player);

            moveSpaceships(spaceship);
            drawSpaceships(terminal, spaceship);

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
        listOfSpaceships.add(new Spaceship(randomX,0,'V'));
        listOfSpaceships.add(new Spaceship(randomX,0,'H'));
        listOfSpaceships.add(new Spaceship(randomX,0,'Ö'));
        return listOfSpaceships.get(randomShip);
    }

    private static void drawPlayer(Terminal terminal, Player player) throws IOException {
        terminal.setCursorPosition(player.getOldX(), player.getOldY());
        terminal.putCharacter(' ');

        terminal.setCursorPosition(player.getX(), player.getY());
        terminal.putCharacter(player.getSymbol());

        terminal.flush();
    }

    private static void drawSpaceships(Terminal terminal, Spaceship spaceship) throws IOException {
        terminal.setCursorPosition(spaceship.getOldX(), spaceship.getOldY());
        terminal.putCharacter(' ');

        terminal.setCursorPosition(spaceship.getX(), spaceship.getY());
        terminal.putCharacter(spaceship.getSymbol());

        terminal.flush();
    }

    private static void movePlayer(Player player, KeyStroke keyStroke) {
        switch (keyStroke.getKeyType()) {
            case ArrowLeft:
                player.moveLeft();
                break;
            case ArrowRight:
                player.moveRight();
                break;
        }
    }

    private static void moveSpaceships(Spaceship spaceship) {
        spaceship.moveDown();
    }

    private static KeyStroke getUserKeyStroke(Terminal terminal, Spaceship spaceship) throws InterruptedException, IOException {
        KeyStroke keyStroke;
        int index = 0;
        do {
            Thread.sleep(5);
            if (index % 100 == 0) {
                moveSpaceships(spaceship);
                drawSpaceships(terminal, spaceship);
            }
            index++;
            keyStroke = terminal.pollInput();
        } while (keyStroke == null);
        return keyStroke;
    }
}

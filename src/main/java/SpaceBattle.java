import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SpaceBattle {

    public static void main(String[] args) throws Exception {

        Terminal terminal = createTerminal();
        Lives lives = new Lives(3);
        Score score = new Score();
        Difficulty difficulty = new Difficulty(1,50); //initiating speed to 60
        Messages messages = new Messages();

        messages.gameTitle(terminal); //to refresh info while playing
        messages.level(terminal, difficulty);
        messages.playerScore(terminal, score);
        messages.lives(terminal, lives);

        Alien alien = createAlien();
        Player player = createPlayer();
        Laser laser = createLaser(player); //sending player to method createLaser to include player x/y values

        drawPlayer(terminal, player);

        messages.objective(terminal); //display objective message
        Thread.sleep(2000); //keep objective visible 2000 millis
        messages.hideMessages(terminal); //hide objective message
        Thread.sleep(200); //wait 200 millis until drawing alien

        drawAlien(terminal, alien);

        do {
            messages.gameTitle(terminal); //to refresh info while playing
            messages.level(terminal, difficulty);
            messages.playerScore(terminal, score);
            messages.lives(terminal, lives);

            if (laser.isActive() == true) { //if laser isActive  ...
                laser = createLaser(player); //... initiate laser
                drawLaser(terminal, laser, alien, player); //laser drawn set to symbol '|'
                Thread.sleep(40); //sleep getting symbol '|' to flash quickly before hiding it
                laser.setSymbol(' '); //setting laser symbol to ' ' (blank space)
                drawLaser(terminal, laser, alien, player); //laser drawn set to symbol ' ' (to hide symbol '|')
                terminal.flush();
            }

            if (difficulty.getLevel() == 1 && score.getPlayerScore() == 10 || difficulty.getLevel() == 2 && score.getPlayerScore() == 20) {
                messages.levelUpMessage(terminal);
                Thread.sleep(2000);
                messages.hideMessages(terminal);
                terminal.flush();
                difficulty.increaseGameSpeed(); //increase speed every 10th score
                difficulty.increaseLevel(); //increase level every 10th score
            }

            if (alien.isHidden() == true) { //alien isHidden if alien and enemy appear at same Y value.
                alien = createAlien();
            }

            KeyStroke keyStroke = getUserKeyStroke(terminal, player, alien, score, lives, difficulty, messages);
            movePlayer(player, alien, laser, score, terminal, keyStroke);
            drawPlayer(terminal, player);
            moveAlien(alien);
            drawAlien(terminal, alien);

            if (score.getPlayerScore() == 30) { //break loop if player get score 30 = player win
                break;
            }

        } while (lives.getLives() != 0); //loop until lives is 0 = player lost

        messages.playerScore(terminal, score);
        messages.lives(terminal, lives);
        messages.gameOver(terminal, score, lives);
    }

    private static Terminal createTerminal() throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
        return terminal;
    }

    private static Player createPlayer() {
        return new Player(38, 23, 'A');
    }

    private static Alien createAlien() {
        int randomX = ThreadLocalRandom.current().nextInt(0, 80);
        int randomShip = ThreadLocalRandom.current().nextInt(0, 3);
        ArrayList<Alien> listOfAliens = new ArrayList<>();
        listOfAliens.add(new Alien(randomX, 0, '¥'));
        listOfAliens.add(new Alien(randomX, 0, 'Ÿ'));
        listOfAliens.add(new Alien(randomX, 0, 'v'));
        listOfAliens.add(new Alien(randomX, 0, 'Ü'));
        return listOfAliens.get(randomShip);
    }

    private static Laser createLaser(Player player) {
        return new Laser(player.getX(), player.getY(), '|');
    }

    private static void drawPlayer(Terminal terminal, Player player) throws IOException {
        terminal.setCursorPosition(player.getOldX(), player.getOldY());
        terminal.putCharacter(' ');

        terminal.setCursorPosition(player.getX(), player.getY());
        terminal.putCharacter(player.getSymbol());

        terminal.flush();
    }

    private static void drawAlien(Terminal terminal, Alien alien) throws IOException {
        terminal.setCursorPosition(alien.getOldX(), alien.getOldY());
        terminal.putCharacter(' ');

        terminal.setCursorPosition(alien.getX(), alien.getY());
        terminal.putCharacter(alien.getSymbol());

        terminal.flush();
    }

    private static void drawLaser(Terminal terminal, Laser laser, Alien alien, Player player) throws InterruptedException, IOException {
        if (alien.getX() == player.getX() || alien.getX() + 1 == player.getX() || alien.getX() - 1 == player.getX()) {
            int distance = player.getY() - alien.getY();
            for (int i = 0; i < distance; i++) {
                terminal.setCursorPosition(laser.getX(), laser.getY() - i - 1);
                terminal.putCharacter(laser.getSymbol());
                terminal.flush();
            }
        } else
            for (int i = 0; i < 22; i++) {
                terminal.setCursorPosition(laser.getX(), laser.getY() - i - 1);
                terminal.putCharacter(laser.getSymbol());
                terminal.flush();
            }
    }

    private static void movePlayer(Player player, Alien alien, Laser laser, Score score, Terminal terminal, KeyStroke keyStroke) throws IOException {
        switch (keyStroke.getKeyType()) {
            case ArrowLeft:
                player.moveLeft();
                alien.setY(alien.getY());
                break;
            case ArrowRight:
                player.moveRight();
                alien.setY(alien.getY());
                break;
            case ArrowUp:
                laser.setActive(true);
                alien.setY(alien.getY());
                if (alien.getX() == player.getX() || alien.getX() + 1 == player.getX() || alien.getX() - 1 == player.getX()) {
                    laser.setActive(true);
                    terminal.bell();
                    alien.setSymbol(' ');
                    alien.setIsHidden(true);
                    score.updatePlayerScore();
                    terminal.flush();
                    break;
                }
        }
    }

    private static void moveAlien(Alien alien) {
        alien.moveDown();
    }

    private static KeyStroke getUserKeyStroke(Terminal terminal, Player player, Alien alien, Score score, Lives lives, Difficulty difficulty, Messages messages) throws InterruptedException, IOException {
        KeyStroke keyStroke;
        int index = 0;
        do {
            Thread.sleep(5);
            if (index % difficulty.getGameSpeed() == 0) { //setting difficulty/alien speed
                moveAlien(alien);
                drawAlien(terminal, alien);
                messages.gameTitle(terminal);
                messages.playerScore(terminal, score);
                messages.lives(terminal, lives);
                if (player.getY() == alien.getY()) {
                    alien.setSymbol(' ');
                    alien.setIsHidden(true);
                    lives.updateLives();
                }
            }
            index++;
            keyStroke = terminal.pollInput();
        } while (keyStroke == null);
        return keyStroke;
    }
}


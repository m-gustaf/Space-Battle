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

        Alien alien = createAlien();
        Player player = createPlayer();
        Laser laser = createLaser(player); //sending player to method createLaser to include player x/y values

        drawPlayer(terminal, player);
        drawAlien(terminal, alien);

        do {
            gameTitle(terminal); //to show info while playing
            playerScore(terminal, score);
            lives(terminal,lives);

            if (laser.isActive() == true) { //if laser isActive is true ...
                laser = createLaser(player); //... create laser
                drawLaser(terminal, laser, alien, player); //laser drawn set to symbol '|'
                Thread.sleep(40); //sleep getting symbol '|' to flash quickly before hiding it
                laser.setSymbol(' '); //setting laser symbol to ' ' (blank space)
                drawLaser(terminal, laser, alien, player); //laser drawn set to symbol ' ' (to hide symbol '|')
                terminal.flush();
            }

            if (alien.isHidden() == true) { //alien isHidden is true if alien and enemy is at same Y value.
                alien = createAlien();
            }

            KeyStroke keyStroke = getUserKeyStroke(terminal, player, alien, score, lives);

            movePlayer(player, alien, laser, score, terminal, keyStroke);
            drawPlayer(terminal, player);

            moveAlien(alien);
            drawAlien(terminal, alien);

            if (score.getPlayerScore() == 10) { //player get score 10 = player win
                break;
            }

        } while (lives.getLives() != 0); //game over when lives is 0

        playerScore(terminal, score);
        lives(terminal,lives);
        gameOver(terminal, score, lives);
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
        return new Laser(player.getX(), player.getY(),'|');
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
        }
        else
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

    private static KeyStroke getUserKeyStroke(Terminal terminal, Player player, Alien alien, Score score, Lives lives) throws InterruptedException, IOException {
        KeyStroke keyStroke;
        int index = 0;
        if (alien.getSymbol() == 'Ÿ') { //to make this alien ship fly faster
            do {
                Thread.sleep(5);
                if (index % 15 == 0) { //to make aliens move while user not giving input to keyboard/set alien ship speed
                    moveAlien(alien);
                    drawAlien(terminal, alien);
                    gameTitle(terminal);
                    playerScore(terminal, score);
                    lives(terminal, lives);
                    if (player.getY() == alien.getY()) { //to hide alien ship when player and alien 'y' are equal
                        alien.setSymbol(' ');
                        alien.setIsHidden(true); //set isHidden to true to create new alien in do-while loop
                        lives.updateLives(); //if alien passes player, life countdown
                    }
                }
                index++;
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);
        } else {
            do {
                Thread.sleep(5);
                if (index % 50 == 0) { //setting alien ship speed
                    moveAlien(alien);
                    drawAlien(terminal, alien);
                    gameTitle(terminal);
                    playerScore(terminal, score);
                    lives(terminal, lives);
                    if (player.getY() == alien.getY()) {
                        alien.setSymbol(' ');
                        alien.setIsHidden(true);
                        lives.updateLives();
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
        String rules = "Eliminate 10 aliens";

        for (int i = 0; i < title.length(); i++) {
            terminal.setCursorPosition(i + 2, 1);
            terminal.putCharacter(title.charAt(i));
        }

        for (int i = 0; i < rules.length(); i++) {
            terminal.setCursorPosition(i + 2, 3);
            terminal.putCharacter(rules.charAt(i));
        }
    }

    private static void playerScore(Terminal terminal, Score score) throws InterruptedException, IOException {
        String displayPlayerScore = "Score: " + score.getPlayerScore();
        for (int i = 0; i < displayPlayerScore.length(); i++) {
            terminal.setCursorPosition(i + 2, 5);
            terminal.putCharacter(displayPlayerScore.charAt(i));
            terminal.flush();
        }
    }

    private static void lives (Terminal terminal, Lives lives) throws InterruptedException, IOException {
        String live = "Lives: " + lives.getLives();
            for (int i = 0; i < live.length(); i++) {
                terminal.setCursorPosition(i + 2, 6);
                terminal.putCharacter(live.charAt(i));
                terminal.flush();
            }
    }

    private static void gameOver(Terminal terminal, Score score, Lives lives) throws InterruptedException, IOException {
        String gameOver = "GAME OVER";
        String playerWon = "Congratulations, you won!";
        String playerLost = "You lost, try again!";

        for (int i = 0; i < gameOver.length(); i++) {
            terminal.setCursorPosition(i + 25, 9);
            terminal.putCharacter(gameOver.charAt(i));
            terminal.flush();
            Thread.sleep(200); //sleep to get effect of slowly printing GAME OVER
        }

        if (lives.getLives() > 0) {
            for (int i = 0; i < playerWon.length(); i++) {
                terminal.setCursorPosition(i + 25, 11);
                terminal.putCharacter(playerWon.charAt(i));
                terminal.flush();
            }
        }
        if (lives.getLives() == 0) {
            for (int i = 0; i < playerLost.length(); i++) {
                terminal.setCursorPosition(i + 25, 11);
                terminal.putCharacter(playerLost.charAt(i));
                terminal.flush();
            }
        }
    }
}


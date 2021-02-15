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

        Alien alien = createAlien();
        Player player = createPlayer();
        Laser laser = createLaser(player); //sending player to createLaser method to include player x/y values

        drawPlayer(terminal, player);
        drawAlien(terminal, alien);

        do {
            gameTitle(terminal); //to show title while playing
            playerScore(terminal, score); //to keep scores updated while playing
            alienScore(terminal, score);

            if (laser.isActive() == true) { //if laser isActive is true ...
                laser = createLaser(player); //... creating laser
                drawLaser(terminal, laser); //laser drawn using symbol '|' (via constructor)
                Thread.sleep(40); //sleep to get symbol '|' flash quickly before hiding it
                laser.setSymbol(' '); //setting laser to symbol ' ' (blank space)
                drawLaser(terminal, laser); //laser drawn using symbol ' ' to hide symbol '|'
                terminal.flush();
            }

            if (alien.isHidden() == true) { //alien isHidden is true if alien and enemy is at same Y value.
                alien = createAlien();
            }

            KeyStroke keyStroke = getUserKeyStroke(terminal, player, alien, score);

            movePlayer(player, alien, laser, score, terminal, keyStroke);
            drawPlayer(terminal, player);

            moveAlien(alien);
            drawAlien(terminal, alien);

        } while (score.getPlayerScore() < 5 && score.getAlienScore() < 5); //first to get score 5 win

        playerScore(terminal, score);
        alienScore(terminal, score);
        gameOver(terminal, score);
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

    private static void drawLaser(Terminal terminal, Laser laser) throws InterruptedException, IOException {
        terminal.setCursorPosition(laser.getX(), laser.getY()-1);
        terminal.putCharacter(laser.getSymbol());

        terminal.flush();
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

    private static KeyStroke getUserKeyStroke(Terminal terminal, Player player, Alien alien, Score score) throws InterruptedException, IOException {
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
                    alienScore(terminal, score);
                    if (player.getY() == alien.getY()) { //to hide alien ship when player and alien 'y' are equal
                        alien.setSymbol(' ');
                        alien.setIsHidden(true); //set isHidden to true to create new alien in do-while loop
                        score.updateAlienScore(); //if alien passes player alien get + 1 score
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
                    alienScore(terminal, score);
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
        String displayPlayerScore = "Your score: " + score.getPlayerScore();
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
        String alienWon = "You lost, try again!";

        for (int i = 0; i < gameOver.length(); i++) {
            terminal.setCursorPosition(i + 25, 8);
            terminal.putCharacter(gameOver.charAt(i));
            terminal.flush();
            Thread.sleep(200); //sleep to get effect of slowly printing GAME OVER
        }

        if (score.getPlayerScore() > score.getAlienScore()) {
            for (int i = 0; i < playerWon.length(); i++) {
                terminal.setCursorPosition(i + 25, 10);
                terminal.putCharacter(playerWon.charAt(i));
                terminal.flush();
            }
        }
        if (score.getAlienScore() > score.getPlayerScore()) {
            for (int i = 0; i < alienWon.length(); i++) {
                terminal.setCursorPosition(i + 25, 10);
                terminal.putCharacter(alienWon.charAt(i));
                terminal.flush();
            }
        }
    }
}


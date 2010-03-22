
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class Juego extends GameCanvas {

    public static final int END_OF_LEVEL_X_VALUE = -1100;
    private Levels gameLevel;
    private Vehicle vehicle;
    private final int ANCHO;        //ancho de la pantalla del cell
    private final int ALTO;         //alto de la pantalla del cell
    private Animador animador;      //estara avisando a cada rato que se actualice y dibuje
    private Graphics g;             //el contexto grafico para hacer los trazos
    private int currentKeyCode;
    private boolean isPaused;
    private Image pausedOpaque;
    private StreetRacer2110 midlet;
    private boolean keyIsPressed = true;
    private int pausedMenuSelectedIndex;
    private PauseMenu pauseMenu;
    private boolean yesNoOptionsIsActive = false;
    private boolean returnToMenuIsActive = false;
    private boolean exitGameIsActive = false;
    private Vector enemies;
    private Vector obstacles;
    private static Random generateRandomCoordinate = new Random();
    private MusicPlayer musicPlayer;
    private boolean musicIsActive;
    private boolean alternateEnemyCreation = false;
    private Display display;
    private int carSelectedIndex;
    private int currentLevel;

    public Juego(StreetRacer2110 midlet, int carSelectedIndex, boolean musicIsActive, int currentLevel) {

        super(true);

        this.midlet = midlet;
        this.display = Display.getDisplay(midlet);

        this.setFullScreenMode(true);

        this.currentLevel = currentLevel;

        pausedMenuSelectedIndex = 0;

        this.carSelectedIndex = carSelectedIndex;
        this.ANCHO = getWidth();
        this.ALTO = getHeight();

        g = this.getGraphics();

        musicPlayer = new MusicPlayer(currentLevel);
        this.musicIsActive = musicIsActive;

        vehicle = new Vehicle(this.ANCHO, this.ALTO, carSelectedIndex);

        enemies = new Vector();
        enemies.addElement(new Enemies(ANCHO + 100, (ALTO - generateRandomCoordinate.nextInt(100) - 60), 0));

        obstacles = new Vector();
        obstacles.addElement(new Obstacles((ANCHO + 100), (ALTO - generateRandomCoordinate.nextInt(100) - 15), 0));

        try {
            pausedOpaque = Image.createImage("/paused.png");
        } catch (IOException ex) {
            System.out.println("Error no puedo cargar la imagen paused.png");
        }
        isPaused = false;

        this.gameLevel = new Levels(currentLevel, ANCHO, ALTO);

        pauseMenu = new PauseMenu(this, g);

        animador = new Animador(this);      //animador debe ser el ultimo que se crea
    }

    public void checkForGameOver() {
        if (vehicle.returnGameOver()) {
            vehicle.setGameOver(false);
            midlet.restartGame();
        }
    }

    public void checkForLevelCompleted() {
        if (gameLevel.returnSkyBackgroundXValue() == END_OF_LEVEL_X_VALUE) {
            nullifyObjects();

            //this if is temporary, to avoid getting a nullpointer in Levels
            if (this.currentLevel < 3) {
                midlet.loadNextLevel(this.carSelectedIndex, this.currentLevel + 1);
            } else {
                midlet.loadNextLevel(this.carSelectedIndex, this.currentLevel);
            }
        }
    }

    public void pauseOrUnpause() {
        currentKeyCode = getKeyStates();
        if ((currentKeyCode & GAME_A_PRESSED) != 0) {
            //Pausar??
            if (keyIsPressed) {
                isPaused = !isPaused;
            }
            keyIsPressed = false;
        }
        if ((currentKeyCode & GAME_A_PRESSED) == 0) {
            keyIsPressed = true;
        }
    }

    public void readProcessKeysPressed() {
        currentKeyCode = getKeyStates();
        if ((currentKeyCode & UP_PRESSED) != 0) {
            vehicle.moveUp();
        }
        if ((currentKeyCode & DOWN_PRESSED) != 0) {
            vehicle.moveDown();
        }
        if ((currentKeyCode & LEFT_PRESSED) != 0) {
            vehicle.moveLeft();
        }
        if ((currentKeyCode & RIGHT_PRESSED) != 0) {
            vehicle.moveRight();
        }
        if ((currentKeyCode & FIRE_PRESSED) != 0) {
            vehicle.setBulletX();
            vehicle.setBulletY();
            vehicle.agregarBullet();
        }
    }

    public void start() {
        if (musicIsActive) {
            musicPlayer.startMusicPlayer();
        }
        animador.iniciar();
    }

    void actualizar() {
        checkForLevelCompleted();
        checkForGameOver();
        pauseOrUnpause();
        currentKeyCode = getKeyStates();
        if (isPaused) {

            if ((currentKeyCode & UP_PRESSED) != 0) {
                pausedMenuSelectedIndex--;
                if (pausedMenuSelectedIndex < 0) {
                    pausedMenuSelectedIndex = 2;
                }
            } else if ((currentKeyCode & DOWN_PRESSED) != 0) {
                pausedMenuSelectedIndex++;
                if (pausedMenuSelectedIndex > 2) {
                    pausedMenuSelectedIndex = 0;
                }
            } else if ((currentKeyCode & FIRE_PRESSED) != 0) {
                if (pausedMenuSelectedIndex == 0) {
                    isPaused = !isPaused;

                } else if (pausedMenuSelectedIndex == 1) {
                    yesNoOptionsIsActive = true;
                    returnToMenuIsActive = true;

                } else if (pausedMenuSelectedIndex == 2) {
                    yesNoOptionsIsActive = true;
                    exitGameIsActive = true;
                }
            }

            if (yesNoOptionsIsActive) {
                if (pausedMenuSelectedIndex == 2) {
                    if ((currentKeyCode & GAME_C_PRESSED) != 0) {
                        midlet.notifyDestroyed();
                    }
                    if ((currentKeyCode & GAME_D_PRESSED) != 0) {
                        yesNoOptionsIsActive = false;
                        exitGameIsActive = false;
                    }
                } else if (pausedMenuSelectedIndex == 1) {
                    if ((currentKeyCode & GAME_C_PRESSED) != 0) {
                        musicPlayer.stopMusicPlayer();
                        musicPlayer = null;
                        animador.terminar();
                        midlet.changeGameToScreen(musicIsActive);
                    }
                    if ((currentKeyCode & GAME_D_PRESSED) != 0) {
                        yesNoOptionsIsActive = false;
                        returnToMenuIsActive = false;
                    }
                }
            }
            return;
        }
        readProcessKeysPressed();

        createObstacles();
        removeObstacles();

        createEnemies();
        removeEnemies();
        addEnemyBullets();

        checkEnemyBulletsVehicleCollision();

        vehicle.checkBulletsEnemyCollision(enemies);

        checkVehicleCollisions();
        checkObstaclesVehicleCollisions();

        gameLevel.actualizar();
        vehicle.actualizarFireGun();
    }

    void dibujar() {
        // Borrar primeramente toda la pantalla
        g.setColor(0x000066);  // R(00) G(FF) B (00)
        g.fillRect(0, 0, ANCHO, ALTO);

        // Despues dibujar todos los objetos de la aplicacion
        gameLevel.dibujar(g);

        drawObstacles();
        vehicle.dibujar(g);
        vehicle.dibujarFireGun(g);
        drawEnemies();

        if (isPaused) {

            g.drawImage(pausedOpaque, 0, 0, g.TOP | g.LEFT);
            if (yesNoOptionsIsActive) {
                if (returnToMenuIsActive) {
                    pausedMenuSelectedIndex = 1;
                } else if (exitGameIsActive) {
                    pausedMenuSelectedIndex = 2;
                }
            }
            pauseMenu.drawPausedMenu(this, pausedMenuSelectedIndex, yesNoOptionsIsActive);
        }
        flushGraphics();    //Actualiza los cambios en la memoria de la pantalla
    }

    //MANEJO DE PAUSA
    //this method is called when my gamecanvas is hidden for any reason
    protected void hideNotify() {
        isPaused = true;
    }

    // this method is called when my gamecanvas return to top priority, when it returns from being hidden
    protected void showNotify() {
    }

    public void createObstacles() {
        if (obstacles.size() < currentLevel) {
            obstacles.addElement(new Obstacles(ANCHO + generateRandomCoordinate.nextInt(120), (ALTO - generateRandomCoordinate.nextInt(100) - 10), 0));
        }
    }

    public void removeObstacles() {
        for (int i = this.obstacles.size() - 1; i >= 0; i--) {
            if ((((Obstacles) obstacles.elementAt(i)).getObstacleX() < -((Obstacles) obstacles.elementAt(i)).getObstacleWidth())) {
                obstacles.removeElementAt(i);
            } else {
                ((Obstacles) obstacles.elementAt(i)).actualizar();
            }
        }
    }

    public void createEnemies() {
        if (enemies.size() < currentLevel) {
            if (alternateEnemyCreation) {
                enemies.addElement(new Enemies(ANCHO + generateRandomCoordinate.nextInt(110), (ALTO - generateRandomCoordinate.nextInt(100) - 65), 0));
            } else {
                enemies.addElement(new Enemies(ANCHO + generateRandomCoordinate.nextInt(110), (ALTO - generateRandomCoordinate.nextInt(100) - 65), 1));

            }
            alternateEnemyCreation = !alternateEnemyCreation;
        }
    }

    public void removeEnemies() {
        for (int i = this.enemies.size() - 1; i >= 0; i--) {
            if ((((Enemies) enemies.elementAt(i)).getEnemyX() < -((Enemies) enemies.elementAt(i)).getEnemyWidth())
                    || ((Enemies) enemies.elementAt(i)).returnEnemyHasCollided()) {
                enemies.removeElementAt(i);
            } else {
                ((Enemies) enemies.elementAt(i)).actualizar();
            }
        }
    }

    public void checkVehicleCollisions() {
        for (int i = this.enemies.size() - 1; i >= 0; i--) {
            if (((Enemies) enemies.elementAt(i)).getEnemyX() <= vehicle.getVehicleX() + vehicle.getVehicleWidth()
                    && ((Enemies) enemies.elementAt(i)).getEnemyX() >= vehicle.getVehicleX()
                    && ((Enemies) enemies.elementAt(i)).getEnemyY() + (((Enemies) enemies.elementAt(i)).getEnemyHeight() / 2) < (vehicle.getVehicleY() + vehicle.getVehicleHeight())
                    && ((Enemies) enemies.elementAt(i)).getEnemyY() + (((Enemies) enemies.elementAt(i)).getEnemyHeight() / 2) > vehicle.getVehicleY()) {

                vehicle.hasCollided(true, true);
                display.vibrate(200);
                ((Enemies) this.enemies.elementAt(i)).hasCollided(true);
            }


        }
    }

    public void checkObstaclesVehicleCollisions() {
        for (int i = this.obstacles.size() - 1; i >= 0; i--) {
            if (((Obstacles) obstacles.elementAt(i)).getObstacleX() <= vehicle.getVehicleX() + vehicle.getVehicleWidth()
                    && ((Obstacles) obstacles.elementAt(i)).getObstacleX() >= vehicle.getVehicleX()
                    && ((Obstacles) obstacles.elementAt(i)).getObstacleY() < (vehicle.getVehicleY()) + vehicle.getVehicleHeight()
                    && ((Obstacles) obstacles.elementAt(i)).getObstacleY() + ((Obstacles) obstacles.elementAt(i)).getObstacleHeight() + 10 > (vehicle.getVehicleY() + vehicle.getVehicleHeight())) {

                if (!((Obstacles) obstacles.elementAt(i)).obstacleHasCollided()) {
                    vehicle.hasCollided(true, false);
                }
                display.vibrate(200);
                ((Obstacles) obstacles.elementAt(i)).hasCollided(true);
            }


        }
    }

    public void addEnemyBullets() {
        for (int i = this.enemies.size() - 1; i >= 0; i--) {
            ((Enemies) enemies.elementAt(i)).setBulletX();
            ((Enemies) enemies.elementAt(i)).setBulletY();
            ((Enemies) enemies.elementAt(i)).agregarBullet();
            ((Enemies) enemies.elementAt(i)).actualizarFireGun();
        }
    }

    private void checkEnemyBulletsVehicleCollision() {
        for (int i = this.enemies.size() - 1; i >= 0; i--) {
            ((Enemies) enemies.elementAt(i)).checkEnemyBulletsVehicleCollision(vehicle);
        }
    }

    private void drawObstacles() {
        for (int i = this.obstacles.size() - 1; i >= 0; i--) {
            ((Obstacles) obstacles.elementAt(i)).dibujar(g);
        }
    }

    private void drawEnemies() {
        for (int i = this.enemies.size() - 1; i >= 0; i--) {
            ((Enemies) enemies.elementAt(i)).dibujar(g);
            ((Enemies) enemies.elementAt(i)).dibujarFireGun(g);
        }
    }

    public void resetJuegoValues() {
        this.vehicle.resetValues();
        this.enemies.removeAllElements();
        this.obstacles.removeAllElements();
        this.gameLevel.resetValues();
        if (musicIsActive) {
            this.musicPlayer.stopMusicPlayer();
            this.musicPlayer = null;
            this.musicPlayer = new MusicPlayer(currentLevel);
            start();
        }
    }

    public void nullifyObjects() {
        this.animador.terminar();
        if (musicIsActive) {
            this.musicPlayer.stopMusicPlayer();
            this.musicPlayer = null;
        }
        System.gc();
    }
}

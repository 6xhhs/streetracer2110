
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class Juego extends GameCanvas {

    public static final int END_OF_LEVEL_X_VALUE = -1100;
    public static final int FINAL_OBSTACLE_START = -800;
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
    private final int currentLevel;
    public int highScore;
    private Ramp ramp;
    private boolean finalObstacleIsActive;
    private boolean vehicleIsAtRamp;

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
        obstacles = new Vector();

        try {
            pausedOpaque = Image.createImage("/paused.png");
        } catch (IOException ex) {
            System.out.println("Error no puedo cargar la imagen paused.png");
        }
        isPaused = false;

        this.gameLevel = new Levels(currentLevel, ANCHO, ALTO);

        pauseMenu = new PauseMenu(this, g);

        ramp = new Ramp(currentLevel, ANCHO, ALTO);
        finalObstacleIsActive = false;
        this.vehicleIsAtRamp = false;

        createObstacles();
        createEnemies();

        highScore = 0;

        animador = new Animador(this);      //animador debe ser el ultimo que se crea
        animador.iniciar();
    }

    public void checkForGameOver() {
        if (vehicle.returnGameOver()) {
            vehicle.setGameOver(false);
            midlet.restartGame();
        }
    }

    public void checkForLevelCompleted() {
        if (gameLevel.returnSkyBackgroundXValue() == END_OF_LEVEL_X_VALUE) {

            highScore += vehicle.returnTotalPointsAccumulated();
            if (this.currentLevel < 3) {
                midlet.loadNextLevel(this.carSelectedIndex, this.currentLevel + 1, highScore);
            } else {
                midlet.loadYouWon(highScore);
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
        if (!this.vehicleIsAtRamp) {
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
        }
        if ((currentKeyCode & FIRE_PRESSED) != 0) {
            vehicle.setBulletX();
            vehicle.setBulletY();
            vehicle.agregarBullet();
        }
    }

    public void start() {

        createObstacles();
        createEnemies();
    }

    public void actualizar() {
        pauseOrUnpause();
        currentKeyCode = getKeyStates();
        if (isPaused) {
            if (musicPlayer.isPlaying()) {
                musicPlayer.stopMusicPlayer();
            }

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

                        midlet.changeGameToScreen();
                    }
                    if ((currentKeyCode & GAME_D_PRESSED) != 0) {
                        yesNoOptionsIsActive = false;
                        returnToMenuIsActive = false;
                    }
                }
            }
            return;
        }

        if (musicIsActive) {
            if ( musicPlayer!=null && !musicPlayer.isPlaying()) {
                musicPlayer.startMusicPlayer();
            }
        }
        readProcessKeysPressed();

        runThroughEnemiesVector();

        vehicle.checkBulletsEnemyCollision(enemies);

        runThroughObstaclesVector();

        gameLevel.actualizar();
        vehicle.actualizarFireGun();

        checkForFinalObstacle();
        actualizarRampVehicleMovements();

        checkForGameOver();
        checkForLevelCompleted();
    }

    void dibujar() {
        // Borrar primeramente toda la pantalla
        g.setColor(0x000066);  // R(00) G(FF) B (00)
        g.fillRect(0, 0, ANCHO, ALTO);

        // Despues dibujar todos los objetos de la aplicacion
        gameLevel.dibujar(g);

        drawObstacles();

        if (finalObstacleIsActive) {
            ramp.dibujar(g);
        }
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
        while (obstacles.size() < currentLevel) {
            obstacles.addElement(new Obstacles(ANCHO + generateRandomCoordinate.nextInt(120), (ALTO - generateRandomCoordinate.nextInt(100) - 10), 0));
        }
    }

    public void resetObstacles(int i) {
        if ((((Obstacles) obstacles.elementAt(i)).getObstacleX() < -((Obstacles) obstacles.elementAt(i)).getObstacleWidth())) {
            ((Obstacles) obstacles.elementAt(i)).resetObstacleCoordinates(ANCHO + generateRandomCoordinate.nextInt(120), (ALTO - generateRandomCoordinate.nextInt(100) - 10));
        } else {
            ((Obstacles) obstacles.elementAt(i)).actualizar();
        }
    }

    public void checkObstaclesVehicleCollisions(int i) {
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

    public void runThroughObstaclesVector() {
        for (int i = this.obstacles.size() - 1; i >= 0; i--) {
            resetObstacles(i);
            checkObstaclesVehicleCollisions(i);
        }
    }

    public void createEnemies() {
        while (enemies.size() < currentLevel) {
            if (alternateEnemyCreation) {
                enemies.addElement(new Enemies(ANCHO + generateRandomCoordinate.nextInt(110), (ALTO - generateRandomCoordinate.nextInt(100) - 65), 0));
            } else {
                enemies.addElement(new Enemies(ANCHO + generateRandomCoordinate.nextInt(110), (ALTO - generateRandomCoordinate.nextInt(100) - 65), 1));
            }
            alternateEnemyCreation = !alternateEnemyCreation;
        }
    }

    public void resetEnemyCoordinates(int i) {
        if ((((Enemies) enemies.elementAt(i)).getEnemyX() < -((Enemies) enemies.elementAt(i)).getEnemyWidth())) {
            ((Enemies) enemies.elementAt(i)).resetEnemyCoordinates(ANCHO + generateRandomCoordinate.nextInt(110), (ALTO - generateRandomCoordinate.nextInt(100) - 65));
        }
    }

    public void resetEnemies(int i) {
        if (((Enemies) enemies.elementAt(i)).returnEnemyHasCollided()) {
            ((Enemies) enemies.elementAt(i)).resetEnemy(ANCHO + generateRandomCoordinate.nextInt(110), (ALTO - generateRandomCoordinate.nextInt(100) - 65));
        } else {
            ((Enemies) enemies.elementAt(i)).actualizar();
        }
    }

    public void checkVehicleCollisions(int i) {
        if (((Enemies) enemies.elementAt(i)).getEnemyX() <= vehicle.getVehicleX() + vehicle.getVehicleWidth()
                && ((Enemies) enemies.elementAt(i)).getEnemyX() >= vehicle.getVehicleX()
                && ((Enemies) enemies.elementAt(i)).getEnemyY() + (((Enemies) enemies.elementAt(i)).getEnemyHeight() / 2) < (vehicle.getVehicleY() + vehicle.getVehicleHeight())
                && ((Enemies) enemies.elementAt(i)).getEnemyY() + (((Enemies) enemies.elementAt(i)).getEnemyHeight() / 2) > vehicle.getVehicleY()) {

            vehicle.hasCollided(true, true);
            display.vibrate(200);
            ((Enemies) this.enemies.elementAt(i)).hasCollided(true);
        }
    }

    public void addEnemyBullets(int i) {
        ((Enemies) enemies.elementAt(i)).setBulletX();
        ((Enemies) enemies.elementAt(i)).setBulletY();
        ((Enemies) enemies.elementAt(i)).agregarBullet();
        ((Enemies) enemies.elementAt(i)).actualizarFireGun();
    }

    private void checkEnemyBulletsVehicleCollision(int i) {
        ((Enemies) enemies.elementAt(i)).checkEnemyBulletsVehicleCollision(vehicle);
    }

    //this method will do all the updates for enemies
    public void runThroughEnemiesVector() {
        for (int i = this.enemies.size() - 1; i >= 0; i--) {
            // adds enemies bullets
            addEnemyBullets(i);

            // checks enemybullets collisions
            checkEnemyBulletsVehicleCollision(i);

            //resets enemies coordinates if they collide
            resetEnemies(i);

            //reset enemy coorinates if enemies go off screen
            resetEnemyCoordinates(i);

            // check for vehicleCollisions
            checkVehicleCollisions(i);
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

        this.vehicleIsAtRamp = false;
        this.finalObstacleIsActive = false;

        this.vehicle.resetValues();
        this.ramp.resetRampCoordinates();

        for (int i = this.enemies.size() - 1; i >= 0; i--) {
            ((Enemies) enemies.elementAt(i)).resetEnemy(ANCHO + generateRandomCoordinate.nextInt(110), (ALTO - generateRandomCoordinate.nextInt(100) - 65));
            ((Enemies) enemies.elementAt(i)).removeBullets();
        }

        for (int i = this.obstacles.size() - 1; i >= 0; i--) {
            ((Obstacles) obstacles.elementAt(i)).resetObstacleCoordinates(ANCHO + generateRandomCoordinate.nextInt(120), (ALTO - generateRandomCoordinate.nextInt(100) - 10));
        }

        this.gameLevel.resetValues();
        if (musicIsActive) {
            this.musicPlayer.stopMusicPlayer();
            this.musicPlayer.terminate();
            this.musicPlayer = new MusicPlayer(currentLevel);
        }
        start();
    }

    public void nullifyObjects(boolean endAnimador, boolean endMusicPlayer) {
        if (endAnimador) {
            this.animador.terminar();
        }

        if (endMusicPlayer) {
            if (musicIsActive) {
                this.musicPlayer.stopMusicPlayer();
                this.musicPlayer.terminate();
            }
        }
        System.gc();
    }

    public void checkForFinalObstacle() {
        if (gameLevel.returnSkyBackgroundXValue() == FINAL_OBSTACLE_START) {
            this.finalObstacleIsActive = true;
            vehicle.hasCollidedWithRamp(true);
        }
    }

    public void checkRampVehicleCollisions() {
        if (ramp.getRampX() <= vehicle.getVehicleX()
                && ramp.getRampX() + (ramp.getRampWidth() / 2) > vehicle.getVehicleX() + (vehicle.getVehicleWidth() / 2)
                && ramp.getRampY() < vehicle.getVehicleY() + (vehicle.getVehicleHeight() / 2)
                && ramp.getRampY() + ramp.getRampHeight() > vehicle.getVehicleY() + (vehicle.getVehicleHeight() / 2)) {

            this.vehicleIsAtRamp = true;
        }
    }

    private void actualizarRampVehicleMovements() {
        if (finalObstacleIsActive) {
            ramp.actualizar();
            checkRampVehicleCollisions();
            if (this.vehicleIsAtRamp) {
                vehicle.actualizar(7);
            }
            if (!vehicle.getIsAtRamp()) {
                this.vehicleIsAtRamp = false;
                this.finalObstacleIsActive = false;
            }
        }
    }
}

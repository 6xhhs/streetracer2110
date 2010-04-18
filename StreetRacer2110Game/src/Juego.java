
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class Juego extends GameCanvas {

    public static final int LEVEL_END = -1100;
    public static final int START_RAMP = -800;
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
    private int psdMenuIndex;
    private PauseMenu pauseMenu;
    private boolean ysNoIsActive = false;
    private boolean retToMenuIsActive = false;
    private boolean exitGameIsActive = false;
    private Vector enemies;
    private Vector obstacles;
    private static final int MAX_RAND_COORDS = 20;
    private static Random randCoord = new Random();
    private int[] randXEnemCoords;
    private int[] randYEnemCoords;
    private int[] randXObstCoords;
    private int[] randYObstCoords;
    private int obstCoordsIndex;
    private int enemCoordsIndex;
    private MusicPlayer musicPlayer;
    private boolean musicIsActive;
    private boolean createAltEnemy = false;
    private Display display;
    private int carSelectedIndex;
    private final int currentLevel;
    public int highScore;
    private Ramp ramp;
    private boolean rampIsActive;
    private boolean vehicleIsAtRamp;
    private boolean removeEnemy;
    private int enemVecSize;
    private int obstVecSize;

    public Juego(StreetRacer2110 midlet, int carSelectedIndex, boolean musicIsActive, int currentLevel) {

        super(true);

        this.midlet = midlet;
        this.display = Display.getDisplay(midlet);

        this.currentLevel = currentLevel;

        this.removeEnemy = false;
        this.setFullScreenMode(true);

        this.carSelectedIndex = carSelectedIndex;
        this.ANCHO = getWidth();
        this.ALTO = getHeight();

        g = this.getGraphics();

        musicPlayer = new MusicPlayer(currentLevel);
        this.musicIsActive = musicIsActive;

        vehicle = new Vehicle(this.ANCHO, this.ALTO, carSelectedIndex);

        this.gameLevel = new Levels(currentLevel, ANCHO, ALTO);

        pauseMenu = new PauseMenu(this, g);

        ramp = new Ramp(currentLevel, ANCHO, ALTO);
        rampIsActive = false;
        this.vehicleIsAtRamp = false;

        highScore = 0;

        try {
            pausedOpaque = Image.createImage("/paused.png");
        } catch (IOException ex) {
            System.out.println("Error no puedo cargar la imagen paused.png");
        }

        isPaused = false;
        psdMenuIndex = 0;

        obstCoordsIndex = 0;
        enemCoordsIndex = 0;
        randXEnemCoords = new int[MAX_RAND_COORDS];
        randYEnemCoords = new int[MAX_RAND_COORDS];
        randXObstCoords = new int[MAX_RAND_COORDS];
        randYObstCoords = new int[MAX_RAND_COORDS];
        for (int i = 0; i < MAX_RAND_COORDS; i++) {
            randXEnemCoords[i] = (ANCHO + randCoord.nextInt(110));
            randYEnemCoords[i] = (ALTO - randCoord.nextInt(100) - 65);
            randXObstCoords[i] = ANCHO + randCoord.nextInt(120);
            randYObstCoords[i] = (ALTO - randCoord.nextInt(100) - 10);
        }

        enemies = new Vector();
        obstacles = new Vector();
        enemVecSize = 0;
        obstVecSize = 0;
        createObstacles();
        createEnemies();
        
        animador = new Animador(this);      //animador debe ser el ultimo que se crea
        animador.iniciar();
    }

    public void start() {

        createObstacles();
        createEnemies();
    }

    public void checkGameOver() {
        if (vehicle.getGameOver()) {
            vehicle.setGameOver(false);
            midlet.restartGame();
        }
    }

    public void checkLevelCompleted() {
        if (gameLevel.returnSkyBackgroundXValue() == LEVEL_END) {

            highScore += vehicle.getTotalPoints();
            if (this.currentLevel < 3) {
                midlet.loadNextLevel(this.carSelectedIndex, this.currentLevel + 1, highScore);
            } else {
                midlet.loadYouWon(highScore);
            }
        }
    }

    public void pauseUnpause() {
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

    public void ctrlKeysPressed() {
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
//            vehicle.setBulletX();
//            vehicle.setBulletY();
            vehicle.addBullet();
        }
    }

    public void update() {
        pauseUnpause();
        currentKeyCode = getKeyStates();
        if (isPaused) {
            if (musicPlayer.isPlaying()) {
                musicPlayer.stopMusicPlayer();
            }

            if ((currentKeyCode & UP_PRESSED) != 0) {
                psdMenuIndex--;
                if (psdMenuIndex < 0) {
                    psdMenuIndex = 2;
                }
            } else if ((currentKeyCode & DOWN_PRESSED) != 0) {
                psdMenuIndex++;
                if (psdMenuIndex > 2) {
                    psdMenuIndex = 0;
                }
            } else if ((currentKeyCode & FIRE_PRESSED) != 0) {
                if (psdMenuIndex == 0) {
                    isPaused = !isPaused;

                } else if (psdMenuIndex == 1) {
                    ysNoIsActive = true;
                    retToMenuIsActive = true;

                } else if (psdMenuIndex == 2) {
                    ysNoIsActive = true;
                    exitGameIsActive = true;
                }
            }

            if (ysNoIsActive) {
                if (psdMenuIndex == 2) {
                    if ((currentKeyCode & GAME_C_PRESSED) != 0) {
                        midlet.notifyDestroyed();
                    }
                    if ((currentKeyCode & GAME_D_PRESSED) != 0) {
                        ysNoIsActive = false;
                        exitGameIsActive = false;
                    }
                } else if (psdMenuIndex == 1) {
                    if ((currentKeyCode & GAME_C_PRESSED) != 0) {

                        midlet.changeGameToScreen();
                    }
                    if ((currentKeyCode & GAME_D_PRESSED) != 0) {
                        ysNoIsActive = false;
                        retToMenuIsActive = false;
                    }
                }
            }
            return;
        }

        if (musicIsActive) {
            if (musicPlayer != null && !musicPlayer.isPlaying()) {
                musicPlayer.startMusicPlayer();
            }
        }
        ctrlKeysPressed();

        runThroughEnemiesVector();

        vehicle.checkBulletsEnemCollision(enemies);

        runThroughObstaclesVector();

        gameLevel.actualizar();
        vehicle.updateAmmo();

        checkForFinalObstacle();
        actualizarRampVehicleMovements();

        checkGameOver();
        checkLevelCompleted();
    }

    void draw() {
        // Borrar primeramente toda la pantalla
        g.setColor(0x000066);  // R(00) G(FF) B (00)
        g.fillRect(0, 0, ANCHO, ALTO);

        // Despues draw todos los objetos de la aplicacion
        gameLevel.dibujar(g);

        drawObstacles();

        if (rampIsActive) {
            ramp.dibujar(g);
        }
        vehicle.draw(g);
        vehicle.drawAmmo(g);
        drawEnemies();

        if (isPaused) {

            g.drawImage(pausedOpaque, 0, 0, g.TOP | g.LEFT);
            if (ysNoIsActive) {
                if (retToMenuIsActive) {
                    psdMenuIndex = 1;
                } else if (exitGameIsActive) {
                    psdMenuIndex = 2;
                }
            }
            pauseMenu.drawPausedMenu(this, psdMenuIndex, ysNoIsActive);
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
            obstacles.addElement(new Obstacles(randXObstCoords[obstCoordsIndex], randYObstCoords[obstCoordsIndex], this.currentLevel));
            updateObstCoords();
        }
    }

    public void resetObstacles(int i) {
        if ((((Obstacles) obstacles.elementAt(i)).getObstacleX() < -((Obstacles) obstacles.elementAt(i)).getObstacleWidth())) {
            ((Obstacles) obstacles.elementAt(i)).resetObstacleCoordinates(randXObstCoords[obstCoordsIndex], randYObstCoords[obstCoordsIndex]);
            updateObstCoords();
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
        obstVecSize = this.obstacles.size() - 1;
        for (int i = this.obstacles.size() - 1; i >= 0; i--) {
            resetObstacles(i);
            checkObstaclesVehicleCollisions(i);
        }
    }

    public void createEnemies() {
        while (this.enemies.size() < currentLevel) {
            if (createAltEnemy) {
                enemies.addElement(new Enemies(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex], 0));
            } else {
                enemies.addElement(new Enemies(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex], 1));
            }
            updateEnemCoords();
            createAltEnemy = !createAltEnemy;
        }
    }

    public void resetEnemyCoordinates(int i) {
        if ((((Enemies) enemies.elementAt(i)).getEnemyX() < -((Enemies) enemies.elementAt(i)).getEnemyWidth())) {
            if (gameLevel.returnSkyBackgroundXValue() <= this.START_RAMP) {
                enemies.removeElementAt(i);
            } else {
                ((Enemies) enemies.elementAt(i)).resetEnemCoords(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex]);
                updateEnemCoords();
            }
        }
    }

    public void resetEnemies(int i) {
        if (((Enemies) enemies.elementAt(i)).returnEnemyHasCollided()) {
            if (gameLevel.returnSkyBackgroundXValue() <= this.START_RAMP) {
                enemies.removeElementAt(i);
                removeEnemy = true;
            } else {
                ((Enemies) enemies.elementAt(i)).resetEnemy(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex]);
                updateEnemCoords();
            }
        } else {
            ((Enemies) enemies.elementAt(i)).update();
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
        ((Enemies) enemies.elementAt(i)).addBullet();
        ((Enemies) enemies.elementAt(i)).updateAmmo();
    }

    private void checkEnemyBulletsVehicleCollision(int i) {
        ((Enemies) enemies.elementAt(i)).checkBulletsVehicleCollisions(vehicle);
    }

    //this method will do all the updates for enemies
    public void runThroughEnemiesVector() {
        enemVecSize = this.enemies.size() - 1;
        for (int i = enemVecSize; i >= 0; i--) {
            // adds enemies bullets
            addEnemyBullets(i);

            // checks enemybullets collisions
            checkEnemyBulletsVehicleCollision(i);

            // check for vehicleCollisions
            checkVehicleCollisions(i);

            //resets enemies coordinates if they collide
            resetEnemies(i);

            if (!removeEnemy) {
                //reset enemy coorinates if enemies go off screen
                resetEnemyCoordinates(i);
            } else {
                removeEnemy = false;
            }
        }
    }

    private void drawObstacles() {
        obstVecSize = this.obstacles.size() - 1;
        for (int i = obstVecSize; i >= 0; i--) {
            ((Obstacles) obstacles.elementAt(i)).dibujar(g);
        }
    }

    private void drawEnemies() {
        enemVecSize = this.enemies.size() - 1;
        for (int i = enemVecSize; i >= 0; i--) {
            ((Enemies) enemies.elementAt(i)).draw(g);
            ((Enemies) enemies.elementAt(i)).drawAmmo(g);
        }
    }

    public void resetJuegoValues() {

        this.vehicleIsAtRamp = false;
        this.rampIsActive = false;

        this.vehicle.resetValues();
        this.ramp.resetRampCoordinates();

        enemVecSize = enemies.size() - 1;
        for (int i = enemVecSize; i >= 0; i--) {
            ((Enemies) enemies.elementAt(i)).resetEnemy(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex]);
            ((Enemies) enemies.elementAt(i)).removeAllBullets();
            updateEnemCoords();
        }

        obstVecSize = obstacles.size() - 1;
        for (int i = obstVecSize; i >= 0; i--) {
            ((Obstacles) obstacles.elementAt(i)).resetObstacleCoordinates(randXObstCoords[obstCoordsIndex], randYObstCoords[obstCoordsIndex]);
            updateObstCoords();
        }

        this.gameLevel.resetValues();
        if (musicIsActive) {
            this.musicPlayer.stopMusicPlayer();
            this.musicPlayer.terminate();
            this.musicPlayer = new MusicPlayer(currentLevel);
        }
        //start();
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
    }

    public void checkForFinalObstacle() {
        if (gameLevel.returnSkyBackgroundXValue() == START_RAMP) {
            this.rampIsActive = true;
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
        if (rampIsActive) {
            ramp.actualizar();
            checkRampVehicleCollisions();
            if (this.vehicleIsAtRamp) {
                vehicle.update(7);
            }
            if (!vehicle.getIsAtRamp()) {
                this.vehicleIsAtRamp = false;
                this.rampIsActive = false;
            }
        }
    }

    private void updateEnemCoords() {
        enemCoordsIndex++;
        if (enemCoordsIndex >= MAX_RAND_COORDS) {
            enemCoordsIndex = 0;
        }
    }

    private void updateObstCoords() {
        obstCoordsIndex++;
        if (obstCoordsIndex >= MAX_RAND_COORDS) {
            obstCoordsIndex = 0;
        }
    }
}


import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/**
 * Se encargara de crear el  vehículo del juego, cargar el nivel,
 * crear a los enemigos y los objetos, así como la música,
 * revisar si se perdió, maneja el menú de pausa, y se encarga de
 * revisar si existen colisiones
 * @author Salvador Aguilar Galindo, Manuel González Solano
 * @version 1.0, Abril 2010
 */
public class Juego extends GameCanvas {

    public static final int LEVEL_END = -1100;
    public static final int START_RAMP = -950;
    private Levels gameLevel;
    private Vehicle vehicle;
    private final int ANCHO;        //ancho de la pantalla del cell
    private final int ALTO;         //alto de la pantalla del cell
    private Animador animador;      //estara avisando a cada rato que se actualice y dibuje
    private Graphics g;             //el contexto grafico para hacer los trazos
    private int currentKeyCode;
    private boolean isPaused;
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
    private Display display;
    private int carSelectedIndex;
    private final int currentLevel;
    public int highScore;
    private Ramp ramp;
    private boolean rampIsActive;
    private boolean vehicleIsAtRamp;
    private boolean madeItToRamp;
    private boolean removeEnemy;
    private int enemVecSize;
    private int obstVecSize;
    private Health health;
    private static final int HEALTH_DELAY = 30;
    private int healthDelayCount;

    /**
     *Constructor, crea una nueva instancia del juego, segun el nivel actual,
     * el carro seleccionado y el estado del audio.
     * @param midlet El midlet del juego.
     * @param carSelectedIndex Asignara el auto dependiendo de la selección del usuario.
     * @param musicIsActive booleano que dira si esta o no activa la música.
     * @param currentLevel Asignara el nivel correspondiente.
     */
    public Juego(StreetRacer2110 midlet, int carSelectedIndex, boolean musicIsActive, int currentLevel) {

        super(true);

        this.setFullScreenMode(true);
        this.midlet = midlet;
        this.display = Display.getDisplay(midlet);
        g = this.getGraphics();

        this.pauseMenu = new PauseMenu(this, g);
        isPaused = false;
        psdMenuIndex = 0;

        this.removeEnemy = false;
        this.ANCHO = getWidth();
        this.ALTO = getHeight();

        musicPlayer = new MusicPlayer(currentLevel);
        this.musicIsActive = musicIsActive;

        this.carSelectedIndex = carSelectedIndex;
        vehicle = new Vehicle(this.ANCHO, this.ALTO, carSelectedIndex);

        this.currentLevel = currentLevel;
        this.gameLevel = new Levels(currentLevel, ANCHO, ALTO);

        ramp = new Ramp(currentLevel, ANCHO, ALTO);
        rampIsActive = false;
        madeItToRamp = false;
        this.vehicleIsAtRamp = false;

        highScore = 0;

        createRandCoords();
        enemies = new Vector();
        obstacles = new Vector();
        obstVecSize = 0;
        createObstacles();
        createEnemies(this.currentLevel);

        health = new Health(randXObstCoords[obstCoordsIndex], randYObstCoords[obstCoordsIndex]);
        updateObstCoords();
        healthDelayCount = 0;

        animador = new Animador(this);      //animador debe ser el ultimo que se crea
        animador.iniciar();
    }

    /**
     *  Manda llamar a los metodos createObstacles y createEnemies.
     */
    public void start() {

        createObstacles();
        createEnemies(this.currentLevel);
    }

    /**
     *  Revisa si se llamo al metodo de auto, returnGameOver,
     * en caso de ser así, coloca setGameOver de juego en false
     * y reinicia el juego desde el midlet.
     */
    public void checkGameOver() {
        if (vehicle.getGameOver()) {
            vehicle.setGameOver(false);
            midlet.restartGame();
        }
    }

    /**
     * Se en casa de revisar si el el fondo de pantalla ha llegado al final
     * con la constante LEVEL_END en caso de ser así, asigna los valores de mejores
     * puntajes, en caso de ser un nivel, inferior al 3, éste carga el siguiente nivel,
     * de lo contrartio el metodo loadYouWon.
     */
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

    /**
     * Revisa si el juego se encuentra en pasua o no de acuerdo a
     * la tecla apretada.
     */
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

    /**
     * Revisa las teclas apretadas cuando el juego no está en paussado,
     * en caso de que no esté en la rampa final del juego.
     */
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
            vehicle.addBullet();
        }
    }

    /**
     * Revisa si el estado actual del juego. Si es que está en pausa, revisa
     * qué teclas se oprimen para realizar una tarea. Luego, actualiza a los
     * objetos que se encuentran dentro del juego actual y busca si el nivel
     * se ha completado o si el jugador a perdido.
     */
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

        if (healthDelayCount == HEALTH_DELAY) {
            healthDelayCount = 0;
            health.setHealthIsActive(true);

        } else {
            if (!health.returnHealthIsActive()) {
                healthDelayCount++;
            } else {
                checkHealthVehicleCollisions();
                resetHealth();
            }
        }


        gameLevel.update();
        vehicle.updateAmmo();

        checkForFinalObstacle();
        actualizarRampVehicleMovements();

        checkGameOver();
        checkLevelCompleted();
    }

    /**
     * dibuja a los objetos del juego, desde los enemigos hasta las imágenes del nivel.
     * También se encarga de draw el menú de pausa si se encuentra activado.
     */
    public void draw() {
        gameLevel.draw(g);

        drawObstacles();

        if(health.returnHealthIsActive())
            health.draw(g);

        if (rampIsActive) {
            ramp.dibujar(g);
        }
        vehicle.draw(g);
        vehicle.drawAmmo(g);
        drawEnemies();

        if (isPaused) {

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

    /**
     * Crea los obstáculos de manera aleatora, y dependiendo del nivel,
     * es el obstáculo que aparece.
     */
    public void createObstacles() {
        while (obstacles.size() < currentLevel) {
            obstacles.addElement(new Obstacles(randXObstCoords[obstCoordsIndex], randYObstCoords[obstCoordsIndex], this.currentLevel));
            updateObstCoords();
        }
    }

    /**
     * En caso de salir de la pantalla reinicia los valores de los obstaculos,
     * para que vuelvan a aprecer.
     * @param i la posición dentro del vector de obstáculos del obstáculo en cuestión
     */
    public void resetObstacles(int i) {
        if ((((Obstacles) obstacles.elementAt(i)).getObstacleX() < -((Obstacles) obstacles.elementAt(i)).getObstacleWidth())) {
            ((Obstacles) obstacles.elementAt(i)).resetObstacleCoordinates(randXObstCoords[obstCoordsIndex], randYObstCoords[obstCoordsIndex]);
            updateObstCoords();
        } else {
            ((Obstacles) obstacles.elementAt(i)).actualizar();
        }
    }

    /**
     * Se encarga de revisar si ha habido una colisión entre algun objeto
     * con el auto del juego.
     * @param i la posición dentro del vector de obstáculos del obstáculo en cuestión.
     */
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

    /**
     * Recorre el vector de los obstáculos para update a los mismos.
     */
    public void runThroughObstaclesVector() {
        obstVecSize = this.obstacles.size() - 1;
        for (int i = this.obstacles.size() - 1; i >= 0; i--) {
            resetObstacles(i);
            checkObstaclesVehicleCollisions(i);
        }
    }

    /**
     * Se encarga de crear los enemigos, de acuerdo
     * al nivel que se encuentre el usuario.
     * @param gameLevel el nivel actual del juego
     */
    public void createEnemies(int gameLevel) {
        if (this.enemies.size() == 0) {
            if (gameLevel == 1) {
                enemies.addElement(new Enemies(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex], 0));
                updateEnemCoords();
                enemVecSize = 1;
            } else if (gameLevel == 2) {
                enemies.addElement(new Enemies(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex], 0));
                updateEnemCoords();
                enemies.addElement(new Enemies(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex], 1));
                enemVecSize = 2;
            } else if (gameLevel == 3) {
                enemies.addElement(new Enemies(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex], 0));
                updateEnemCoords();
                enemies.addElement(new Enemies(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex], 1));
                updateEnemCoords();
                enemies.addElement(new Enemies(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex], 0));
                enemVecSize = 3;
            }
        }
    }

    /**
     * Reinicia la posición del enemigo si es que se ha salido de pantalla,
     * tomando en cuenta si se ha llegado al obstaculo final.
     * @param i la posición del enemigo dentro del vector de enemigos
     */
    public void resetEnemyCoordinates(int i) {
        if ((((Enemies) enemies.elementAt(i)).getEnemyX() < -((Enemies) enemies.elementAt(i)).getEnemyWidth())) {
            if (madeItToRamp) {
                enemies.removeElementAt(i);
                enemVecSize--;
            } else {
                ((Enemies) enemies.elementAt(i)).resetEnemCoords(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex]);
                updateEnemCoords();
            }
        }
    }

    /**
     * Reinicia a los enemigos que hayan colisionado, tomando en cuenta
     * si se ha llegado al obstaculo final.
     * @param i la posición del enemigo dentro del vector de enemigos
     */
    public void resetCollidedEnems(int i) {
        if (((Enemies) enemies.elementAt(i)).returnEnemyHasCollided()) {
            if (madeItToRamp) {
                enemies.removeElementAt(i);
                removeEnemy = true;
                enemVecSize--;
            } else {
                ((Enemies) enemies.elementAt(i)).resetEnemy(randXEnemCoords[enemCoordsIndex], randYEnemCoords[enemCoordsIndex]);
                updateEnemCoords();
            }
        } else {
            ((Enemies) enemies.elementAt(i)).update();
        }
    }

    /**
     * Revisa las posiciones del enemigo y del vehículo para determinar si
     * hay una colisión, lo cual activaría las variables de colisiones del
     * vehículo y del enemigo además de provocar que el celular vibre.
     * @param i la posición dentro del vector de enemigos del enemigo en cuestión
     */
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

    /**
     * crea una nueva para un enemigo dado y actualiza el estado de sus balas.
     * @param i la posición dentro del vector de enemigos del enemigo.
     */
    public void addEnemyBullets(int i) {
        ((Enemies) enemies.elementAt(i)).addBullet();
        ((Enemies) enemies.elementAt(i)).updateAmmo();
    }

    /**
     * determina si hay una colisión entre una bala del enemigo y el vehículo.
     * @param i la posición del enemigo dentro del vector de enemigos al cual
     * le pertenece la bala en cuestión
     */
    private void checkEnemyBulletsVehicleCollision(int i) {
        ((Enemies) enemies.elementAt(i)).checkBulletsVehicleCollisions(vehicle);
    }

    /**
     * recorre el vector de enemigos para ejecutar las actualizaciones y
     * reiniciaciones de los enemigos dentro del juego.
     */
    public void runThroughEnemiesVector() {
        for (int i = 0; i < enemVecSize; i++) {
            if (this.currentLevel > 1) {
                // adds enemies bullets
                addEnemyBullets(i);
            }
            // checks enemybullets collisions
            checkEnemyBulletsVehicleCollision(i);

            // check for vehicleCollisions
            checkVehicleCollisions(i);

            //resets enemies coordinates if they collide
            resetCollidedEnems(i);

            if (!removeEnemy) {
                //reset enemy coorinates if enemies go off screen
                resetEnemyCoordinates(i);
            } else {
                removeEnemy = false;
            }
        }
    }

    /**
     * dibuja a los obstáculos en la pantalla del juego.
     */
    private void drawObstacles() {
        obstVecSize = this.obstacles.size() - 1;
        for (int i = obstVecSize; i >= 0; i--) {
            ((Obstacles) obstacles.elementAt(i)).dibujar(g);
        }
    }

    /**
     * dibuja a los enemigos en la pantalla del juego.
     */
    private void drawEnemies() {
        enemVecSize = this.enemies.size();
        for (int i = 0; i < enemVecSize; i++) {
            ((Enemies) enemies.elementAt(i)).draw(g);
            ((Enemies) enemies.elementAt(i)).drawAmmo(g);
        }
    }

    /**
     * reinicia los objetos del juego actual
     */
    public void resetJuegoValues() {

        this.vehicle.resetValues();
        this.ramp.resetRampCoordinates();

        enemies.removeAllElements();
        enemVecSize = 0;
        this.vehicleIsAtRamp = false;
        this.rampIsActive = false;
        this.madeItToRamp = false;

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
    }

    /**
     * nulifica al animador y/o reproductor de música.
     * @param endAnimador bandera que dice si el animador será destruido
     * @param endMusicPlayer bandera que dice si el reproductor de música
     * sera destruido
     */
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

    /**
     * analiza la posicion del fondo del cielo para deterimar si se
     * debe activar el último obstáculo o no.
     */
    public void checkForFinalObstacle() {
        if (gameLevel.returnSkyBackgroundXValue() == START_RAMP) {
            this.rampIsActive = true;
            this.madeItToRamp = true;
            vehicle.hasCollidedWithRamp(true);
        }
    }

    /**
     * compara las posiciones de la rampa y del vehículo para determinar si
     * el vehículo ha llegado a la rampa o no.
     */
    public void checkRampVehicleCollisions() {
        if (ramp.getRampX() <= vehicle.getVehicleX()
                && ramp.getRampX() + (ramp.getRampWidth() / 2) > vehicle.getVehicleX() + (vehicle.getVehicleWidth() / 2)
                && ramp.getRampY() < vehicle.getVehicleY() + (vehicle.getVehicleHeight() / 2)
                && ramp.getRampY() + ramp.getRampHeight() > vehicle.getVehicleY() + (vehicle.getVehicleHeight() / 2)) {

            this.vehicleIsAtRamp = true;
        }
    }

    /**
     * controla la imagen del vehículo segun su posición con respecto a la rampa.
     */
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

    /**
     * actualiza al índice del vector de posiciones aleatorias que se usan para
     * ubicar a los enemigos.
     */
    private void updateEnemCoords() {
        enemCoordsIndex++;
        if (enemCoordsIndex >= MAX_RAND_COORDS) {
            enemCoordsIndex = 0;
        }
    }

    /**
     * actualiza al índice del vector de posiciones aleatorioas que se usan para
     * ubicar a los obstáculos
     */
    private void updateObstCoords() {
        obstCoordsIndex++;
        if (obstCoordsIndex >= MAX_RAND_COORDS) {
            obstCoordsIndex = 0;
        }
    }

    private void createRandCoords() {
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
    }

    private void resetHealth() {
        if (health.getXPos() < -health.getWidth()) {
            health.resetHealthObj(randXObstCoords[obstCoordsIndex], randYObstCoords[obstCoordsIndex]);
            updateObstCoords();
        } else {
            health.update();
        }
    }

    private void checkHealthVehicleCollisions() {
        if (health.getXPos() <= vehicle.getVehicleX() + vehicle.getVehicleWidth()
                && health.getXPos() >= vehicle.getVehicleX()
                && health.getYPos() < vehicle.getVehicleY() + vehicle.getVehicleHeight()
                && health.getYPos() + health.getHeight() + 10 > vehicle.getVehicleY() + vehicle.getVehicleHeight()) {

            vehicle.increaseHealth();
            health.resetHealthObj(randXObstCoords[obstCoordsIndex], randYObstCoords[obstCoordsIndex]);
            updateObstCoords();
            display.vibrate(200);
        }
        //display.vibrate(200);
    }

}


import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class Juego extends GameCanvas {

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
    private int createEnemyCount;
    private Vector obstacles;
    private int createObstaclesCount;
    private static Random generateRandomYCoordinate = new Random();
    private MusicPlayer musicPlayer;
    private boolean musicIsActive;
    private boolean alternateEnemyCreation = false;
    private Display display;
    private int carSelectedIndex;

    public Juego(StreetRacer2110 midlet, int carSelectedIndex, boolean musicIsActive) {

        super(true);

        this.midlet = midlet;
        this.display = Display.getDisplay(midlet);

        this.setFullScreenMode(true);

        pausedMenuSelectedIndex = 0;

        this.carSelectedIndex=carSelectedIndex;
        this.ANCHO = getWidth();
        this.ALTO = getHeight();

        g = this.getGraphics();

        musicPlayer = new MusicPlayer(2);
        this.musicIsActive = musicIsActive;

        vehicle = new Vehicle(this.ANCHO, this.ALTO, carSelectedIndex);

        enemies = new Vector();
        enemies.addElement(new Enemies(ANCHO + 80, (ALTO - generateRandomYCoordinate.nextInt(100) - 50), 0));
        createEnemyCount = 0;

        obstacles = new Vector();
        obstacles.addElement(new Obstacles((ANCHO + 80), (ALTO - generateRandomYCoordinate.nextInt(100) - 10), 0));
        createObstaclesCount = 0;

        try {
            pausedOpaque = Image.createImage("/paused.png");
        } catch (IOException ex) {
            System.out.println("Error no puedo cargar la imagen paused.png");
        }
        isPaused = false;

        this.gameLevel = new Levels(2, ANCHO, ALTO);

        pauseMenu = new PauseMenu(this, g);

        animador = new Animador(this);      //animador debe ser el ultimo que se crea
        animador.iniciar();
    }

    public void pauseOrUnpause() {
        currentKeyCode = getKeyStates();
        if ((currentKeyCode & GAME_A_PRESSED) != 0) {
            //Pausar??
            System.out.println("currentKeyCode: " + currentKeyCode);
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
        System.out.println("entered juego.start, so music should play");
    }

    void actualizar() {
//        if(vehicle.returnGameOver()){
//            //vehicle.setGameOver(false);
//            midlet.restartGame(carSelectedIndex, musicIsActive);
//            return;
//        }
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

        checkCollisions();

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
        createObstaclesCount++;
        if (createObstaclesCount == 120) {
            obstacles.addElement(new Obstacles(ANCHO + 20, (ALTO - generateRandomYCoordinate.nextInt(100) - 10), 0));
            createObstaclesCount = 0;
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
        createEnemyCount++;
        if (createEnemyCount == 80) {
            if (alternateEnemyCreation) {
                enemies.addElement(new Enemies(ANCHO + 20, (ALTO - generateRandomYCoordinate.nextInt(100) - 50), 0));
            } else {
                enemies.addElement(new Enemies(ANCHO + 20, (ALTO - generateRandomYCoordinate.nextInt(100) - 50), 1));

            }
            alternateEnemyCreation = !alternateEnemyCreation;
            createEnemyCount = 0;
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

    public void checkCollisions() {
        for (int i = this.enemies.size() - 1; i >= 0; i--) {
            if (((Enemies) enemies.elementAt(i)).getEnemyX() <= vehicle.getVehicleX() + vehicle.getVehicleWidth()
                    && ((Enemies) enemies.elementAt(i)).getEnemyX() >= vehicle.getVehicleX()
                    && ((Enemies) enemies.elementAt(i)).getEnemyY() + (((Enemies) enemies.elementAt(i)).getEnemyHeight() / 2) < (vehicle.getVehicleY() + vehicle.getVehicleHeight())
                    && ((Enemies) enemies.elementAt(i)).getEnemyY() + (((Enemies) enemies.elementAt(i)).getEnemyHeight() / 2) > vehicle.getVehicleY()) {

                vehicle.hasCollided(true);
                display.vibrate(200);
                ((Enemies) this.enemies.elementAt(i)).hasCollided(true);
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

    public void nullifyObjects(){
        
        this.animador.terminar();
//        this.vehicle=null;
//        this.pausedOpaque=null;
//        this.pauseMenu=null;
//        this.obstacles=null;
        this.musicPlayer=null;
//        this.gameLevel=null;
//        this.enemies=null;

    }
}

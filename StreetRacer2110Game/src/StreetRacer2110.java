import java.util.Vector;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
/*
 * Se encarga de dar inicio a la aplicación, así como
 * cargar las pantallas de inicio.
 *@author Salvador Aguilar Galindo, Manuel González Solano
 *@version 1.0, Abril 2010
 */

public class StreetRacer2110 extends MIDlet implements CommandListener {

    private Display display;
    private GUI gui;
    private Juego juego;
    private SplashScreen splashScreen;
    private boolean isStartOfGame = true;
    private int carSelectedIndex;
    private boolean musicIsActive;
    private int currentLevel;
    private FileManager fileManager;
    private int totalPoints;
    private Vector highScorePoints;
    private Vector highScoreNames;
    private int highScorePosition = -1;
    private Form forma;
    private TextField enterHighScoreName;
    private Command OKButton;
    private boolean OKButtonIsPressed;
    private int hiScrVecSize;
    private int songIndex;

    /**
     * Constructor, crea los vectores de los mejores puntajes y la pantalla para ingresar
     * el nombre del jugador.
     */
    public StreetRacer2110() {
        hiScrVecSize = 0;
        highScorePoints = new Vector();
        highScoreNames = new Vector();
        forma = new Form("High Score");
        enterHighScoreName = new TextField("Enter your name: ", null, 3, TextField.ANY);
        forma.append(enterHighScoreName);
        OKButton = new Command("OK", Command.OK, 0);
        forma.addCommand(OKButton);
        forma.setCommandListener(this);
        totalPoints = 0;
        OKButtonIsPressed = false;
        songIndex=1;
    }

    /**
     * se establece como el objeto a ser desplegado en pantalla, recupera
     * los valores de high score desde un archivo e inicia a la aplicación.
     */
    public void startApp() {

        display = Display.getDisplay(this);

        fileManager = new FileManager("data.txt");
        fileManager.readFile(highScorePoints, highScoreNames);
        loadStartOfGame();
    }

    /**
     * carga las primeras imágenes del juego, las cuales son splash screens,
     * seguidas por el menú principal.
     */
    private void loadStartOfGame() {
        if (isStartOfGame) {

            loadNewSplashScreen(1,2000);
            loadNewSplashScreen(2,1000);

            gui = new GUI(this, highScorePoints, highScoreNames);
            gui.start();
            display.setCurrent(gui);
            display.vibrate(700);
            splashScreen = null;
            isStartOfGame = false;
            System.gc();
        } else {
            gui.start();
        }
    }

    /**
     * crea y despliega una nueva splash screen determinada por el índice de
     * selección.
     * @param splashScreenIndex indica qué splash screen se debe de crear.
     */
    private void loadNewSplashScreen(int splashScreenIndex, int delay) {
        splashScreen = new SplashScreen(splashScreenIndex);
        splashScreen.paint();
        display.setCurrent(splashScreen);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
        }
    }

    public void pauseApp() {
    }

    /**
     * acaba con la aplicación.
     * @param unconditional
     */
    public void destroyApp(boolean unconditional) {

        display.setCurrent(null);
        notifyDestroyed();
    }

    /**
     * cambia al menú principal por el juego como objeto que se despliega
     * en pantalla.
     * @param carSelectedIndex indica qué auto fue seleccionado
     * @param musicIsActive indica el estado del audio
     */
    public void changeScreenToGame(int carSelectedIndex, boolean musicIsActive) {
        this.musicIsActive = musicIsActive;
        gui.setItemsToNull();
        gui = null;
        juego = new Juego(this, carSelectedIndex, this.musicIsActive, 1, songIndex);
        updateSongIndex();
        display.setCurrent(juego);
        juego.start();
    }

    private void updateSongIndex() {
        songIndex++;
        if (songIndex > 5) {
            songIndex = 1;
        }
    }

    /**
     * cambia al juego por el menú principal como el objeto que se despliega
     * en pantalla.
     */
    public void changeGameToScreen() {

        juego.nullifyObjects(true, true);
        juego = null;
        gui = new GUI(this, highScorePoints, highScoreNames);
        gui.setMusicIsActive(musicIsActive);
        display.setCurrent(gui);
        totalPoints = 0;
        gui.start();
        //this.OKButtonIsPressed = false;
    }

    /**
     * reinicia al juego, cargando la splash screen adecuada mientras carga.
     */
    public void restartGame() {

        loadNewSplashScreen(3,2000);
        juego.resetJuegoValues(songIndex);
        updateSongIndex();
        display.setCurrent(juego);
        juego.start();
        splashScreen = null;
    }

    /**
     * crea y carga el siguiente nivel del juego.
     * @param carSelectedIndex indica el auto selecciondado
     * @param currentLevel indica el nivel actual del juego
     * @param highScore el puntaje total del juego
     */
    public void loadNextLevel(int carSelectedIndex, int currentLevel, int highScore) {
        splashScreen = new SplashScreen(2 + currentLevel);
        splashScreen.paint();
        display.setCurrent(splashScreen);

        totalPoints += highScore;
        this.carSelectedIndex = carSelectedIndex;
        this.currentLevel = currentLevel;
        juego.nullifyObjects(true, true);
        juego = null;
        juego = new Juego(this, this.carSelectedIndex, this.musicIsActive, this.currentLevel,songIndex);
        updateSongIndex();
        display.setCurrent(juego);
        juego.start();
        splashScreen = null;
    }

    /**
     * carga la pantalla de juego ganado, y si el puntaje acumulado está entre
     * los 5 mejores, carga la pantalla para agregar el nombre del jugador.
     * @param totalPointsAccumulated puntaje total del juego acabado
     */
    public void loadYouWon(int totalPointsAccumulated) {
        this.totalPoints += totalPointsAccumulated;

        boolean isAHighScore = false;
        hiScrVecSize = highScorePoints.size();
        for (int i = 0; i < hiScrVecSize; i++) {
            if (totalPoints >= Integer.parseInt((String) highScorePoints.elementAt(i))) {
                highScorePoints.insertElementAt(totalPoints + "", i);
                isAHighScore = true;
                highScorePosition = i;
                highScorePoints.setSize(5);
                break;
            }
        }

        if (isAHighScore) {
            OKButtonIsPressed=false;
            loadEnterHighScoreNameScreen();
        } else {
            loadYouWonNoHiScore();
        }
    }

    /**
     * despliega la pantalla en donde el jugador escribirá su nombre, y luego
     * destruye al juego actual.
     */
    private void loadEnterHighScoreNameScreen() {
        display.setCurrent(forma);
        juego.nullifyObjects(true, false);
    }

    /**
     * determina qué botón fue oprimido, tomando las acciones necesarias.
     * @param cmnd indica que botón fue oprimido
     * @param dsplbl
     */
    public void commandAction(Command cmnd, Displayable dsplbl) {

        if (!OKButtonIsPressed) {
            if (cmnd == OKButton) {
                String highScoreName = enterHighScoreName.getString();
                highScoreNames.insertElementAt(highScoreName, highScorePosition);
                highScoreNames.setSize(5);

                fileManager.writeToFile(highScorePoints, highScoreNames);
                fileManager.readFile(highScorePoints, highScoreNames);
                OKButtonIsPressed = true;
            }
            changeGameToScreen();
            splashScreen = null;
        }
    }

    private void loadYouWonNoHiScore() {
        loadNewSplashScreen(6,3000);
        changeGameToScreen();
        splashScreen = null;
    }
}

import java.util.Vector;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

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

    public StreetRacer2110() {
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



    }

    public void startApp() {

        display = Display.getDisplay(this);

        fileManager = new FileManager("data.txt");
        fileManager.readFile(highScorePoints, highScoreNames);
        loadStartOfGame();

    }

    private void loadStartOfGame() {
        if (isStartOfGame) {

            loadNewSplashScreen(1);
            loadNewSplashScreen(2);

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

    private void loadNewSplashScreen(int splashScreenIndex) {
        splashScreen = new SplashScreen(splashScreenIndex);
        splashScreen.paint();
        display.setCurrent(splashScreen);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {

        display.setCurrent(null);
        notifyDestroyed();
    }

    public void changeScreenToGame(int carSelectedIndex, boolean musicIsActive) {
        this.musicIsActive = musicIsActive;
        gui.setItemsToNull();
        gui = null;
        System.gc();
        juego = new Juego(this, carSelectedIndex, this.musicIsActive, 1);

        display.setCurrent(juego);
        juego.start();
    }

    public void changeGameToScreen() {

        juego.nullifyObjects(true, true);
        juego = null;
        System.gc();
        gui = new GUI(this, highScorePoints, highScoreNames);
        gui.setMusicIsActive(musicIsActive);
        display.setCurrent(gui);
        totalPoints = 0;
        gui.start();
    }

    public void restartGame() {

        splashScreen = new SplashScreen(3);
        splashScreen.paint();
        display.setCurrent(splashScreen);
        juego.resetJuegoValues();
        display.setCurrent(juego);
        splashScreen = null;
        System.gc();
    }

    public void loadNextLevel(int carSelectedIndex, int currentLevel, int highScore) {
        totalPoints += highScore;
        this.carSelectedIndex = carSelectedIndex;
        this.currentLevel = currentLevel;
        splashScreen = new SplashScreen(2 + currentLevel);
        splashScreen.paint();
        display.setCurrent(splashScreen);
        juego.nullifyObjects(true, true);
        juego = null;
        juego = new Juego(this, this.carSelectedIndex, this.musicIsActive, this.currentLevel);


        display.setCurrent(juego);
        juego.start();

        splashScreen = null;
        System.gc();


    }

    public void loadYouWon(int totalPointsAccumulated) {
        this.totalPoints += totalPointsAccumulated;

        boolean isAHighScore = false;

        for (int i = 0; i < highScorePoints.size(); i++) {
            if (totalPoints >= Integer.parseInt((String) highScorePoints.elementAt(i))) {
                highScorePoints.insertElementAt(totalPoints + "", i);
                isAHighScore = true;
                highScorePosition = i;
                highScorePoints.setSize(5);
                break;
            }
        }

        if (isAHighScore) {
            loadEnterHighScoreNameScreen();
        } else {
            changeGameToScreen();
        }
    }

    private void loadEnterHighScoreNameScreen() {
        juego.nullifyObjects(true, false);
        display.setCurrent(forma);
    }

    public void commandAction(Command cmnd, Displayable dsplbl) {

        if (cmnd == OKButton) {
            if (!OKButtonIsPressed) {
                String highScoreName = enterHighScoreName.getString();
                highScoreNames.insertElementAt(highScoreName, highScorePosition);
                highScoreNames.setSize(5);

                fileManager.writeToFile(highScorePoints, highScoreNames);
                fileManager.readFile(highScorePoints, highScoreNames);
            }
            OKButtonIsPressed = true;
        }

        changeGameToScreen();
        OKButtonIsPressed = false;
    }
}

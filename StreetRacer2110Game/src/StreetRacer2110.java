
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
    private String highScore = "";
    private int totalPoints;
    private Vector highScorePoints;
    private Vector highScoreNames;
    private int highScorePosition = -1;
    Form forma;
    TextField enterHighScoreName;
    Command OKButton;

    public StreetRacer2110() {
        highScorePoints = new Vector();
        highScoreNames = new Vector();
        forma = new Form("High Score");
        enterHighScoreName = new TextField("Enter your name: ", null, 3, TextField.ANY);
        forma.append(enterHighScoreName);
        OKButton = new Command("OK", Command.OK, 0);
        forma.addCommand(OKButton);
        forma.setCommandListener(this);




    }

    public void startApp() {

        display = Display.getDisplay(this);

        fileManager = new FileManager("data.txt");
        //fileManager.writeToFile(totalPoints);
        fileManager.readFile(highScorePoints, highScoreNames);
        System.out.println("has read the file");
        highScore = fileManager.returnReadData();
        for (int i = 0; i < highScorePoints.size(); i++) {
            System.out.println(highScoreNames.elementAt(i) + " " + highScorePoints.elementAt(i));
        }

        if (isStartOfGame) {
            splashScreen = new SplashScreen(1);
            splashScreen.paint();
            display.setCurrent(splashScreen);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            splashScreen = new SplashScreen(2);
            splashScreen.paint();
            display.setCurrent(splashScreen);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            gui = new GUI(this, highScore);
            gui.start();
            display.setCurrent(gui);
            display.vibrate(700);
            splashScreen = null;
            System.gc();
            isStartOfGame = false;
        } else {
            gui = new GUI(this, highScore);
            gui.start();
            display.setCurrent(gui);
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
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        juego.start();
        display.setCurrent(juego);
    }

    public void changeGameToScreen() {
        //juego.nullifyObjects();
        juego = null;
        System.gc();
        gui = new GUI(this, highScore);
        gui.setMusicIsActive(musicIsActive);
        display.setCurrent(gui);
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

    public void loadNextLevel(int carSelectedIndex, int currentLevel) {
        this.carSelectedIndex = carSelectedIndex;
        System.out.println("Old level: " + this.currentLevel);
        this.currentLevel = currentLevel;
        System.out.println("New level: " + this.currentLevel);
        splashScreen = new SplashScreen(2 + currentLevel);
        splashScreen.paint();
        display.setCurrent(splashScreen);
        juego = null;
        System.gc();
        juego = new Juego(this, this.carSelectedIndex, this.musicIsActive, this.currentLevel);

        juego.start();
        //juego.resetJuegoValues();
        display.setCurrent(juego);

        splashScreen = null;
        System.gc();


    }

    public void loadYouWon(Graphics g, int totalPointsAccumulated) {
        this.totalPoints = totalPointsAccumulated;
        System.out.println("YOU WON!! CONGRATS!!!");
        System.out.println(totalPoints);
        //Scanner entrada = new Scanner(System.in);

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

        }
    }

    private void loadEnterHighScoreNameScreen() {
        //juego.nullifyObjects();
        display.setCurrent(forma);
    }

    public void commandAction(Command cmnd, Displayable dsplbl) {

        if (cmnd == OKButton) {
            String highScoreName = enterHighScoreName.getString();
            if (highScorePosition > -1) {
                highScoreNames.insertElementAt(highScoreName, highScorePosition);
                highScoreNames.setSize(5);
            }

            fileManager.writeToFile(highScorePoints, highScoreNames);
            fileManager.readFile(highScorePoints, highScoreNames);
            highScore = fileManager.returnReadData();
            changeGameToScreen();
        }
    }
}

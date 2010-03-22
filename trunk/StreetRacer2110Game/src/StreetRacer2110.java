
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class StreetRacer2110 extends MIDlet {

    private Display display;
    private GUI gui;
    private Juego juego;
    private SplashScreen splashScreen;
    private boolean isStartOfGame = true;
    private int carSelectedIndex;
    private boolean musicIsActive;
    private int currentLevel;

    public void startApp() {

        display = Display.getDisplay(this);

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
            gui = new GUI(this);
            gui.start();
            display.setCurrent(gui);
            display.vibrate(700);
            splashScreen = null;
            System.gc();
            isStartOfGame = false;
        } else {
            gui = new GUI(this);
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
        this.musicIsActive=musicIsActive;
        gui.setItemsToNull();
        gui = null;
        System.gc();
        juego = new Juego(this, carSelectedIndex, this.musicIsActive,1);
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        juego.start();
        display.setCurrent(juego);
    }

    public void changeGameToScreen(boolean musicIsActive) {
        juego = null;
        System.gc();
        gui = new GUI(this);
        gui.setMusicIsActive(musicIsActive);
        display.setCurrent(gui);
        gui.start();
    }

    public void restartGame(){

        splashScreen=new SplashScreen(3);
        splashScreen.paint();
        display.setCurrent(splashScreen);
        juego.resetJuegoValues();
        display.setCurrent(juego);
        splashScreen=null;
        System.gc();
    }

    void loadNextLevel(int carSelectedIndex, int currentLevel) {
        this.carSelectedIndex=carSelectedIndex;
        this.currentLevel=currentLevel;
        splashScreen = new SplashScreen(2+currentLevel);
        splashScreen.paint();
        display.setCurrent(splashScreen);

        juego=null;
        System.gc();
        juego = new Juego(this,this.carSelectedIndex,this.musicIsActive,this.currentLevel);
        display.setCurrent(juego);
        juego.start();
        splashScreen=null;
        System.gc();


    }
}

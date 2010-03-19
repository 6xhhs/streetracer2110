
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class StreetRacer2110 extends MIDlet {

    private Display display;
    private GUI gui;
    private Juego juego;
    private SplashScreen splashScreen;
    private boolean isStartOfGame = true;

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
        gui.setItemsToNull();
        gui = null;
        System.gc();
        juego = new Juego(this, carSelectedIndex, musicIsActive);
        juego.start();
        display.setCurrent(juego);
    }

    public void changeGameToScreen(boolean musicIsActive) {
        //juego.setItemsToNull();
        juego = null;
        System.gc();
        gui = new GUI(this);
        gui.setMusicIsActive(musicIsActive);
        display.setCurrent(gui);
        gui.start();
    }

    public void restartGame(int carSelectIndex, boolean musicIsActive){
        int carSelectedIndex = carSelectIndex;
        boolean activateMusic = musicIsActive;
        splashScreen=new SplashScreen(2);
        splashScreen.paint();
        display.setCurrent(splashScreen);
        juego.nullifyObjects();
        juego=null;
        System.gc();
        juego = new Juego(this,carSelectedIndex,activateMusic);
        display.setCurrent(juego);
        splashScreen=null;
        System.gc();
    }
}




import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.*;

public class GUI extends GameCanvas {

    private StreetRacer2110 midlet;
    private Graphics g;
    private int width = 0;
    private int height = 0;
    private Menu menu;
    private String leftOption;
    private String rightOption;
    private String[] menuOptions;
    private int currentlySelectedIndex = 0;
    private boolean menuIsActive = true;
    private boolean inactiveMenuIsActive = false;
    private boolean carMenuIsActive = false;
    private boolean optionsMenuIsActive = false;
    private boolean keyIsPressed = true;
    private String[] carMenuOptions;
    private int carSelectedIndex = 0;

    private MusicPlayer musicPlayer;
    private boolean musicIsActive;
    private boolean yesNoMenuIsActive = false;

    public GUI(StreetRacer2110 midlet, Vector highScorePoints, Vector highScoreNames) {

        super(false);

        this.midlet = midlet;
        setFullScreenMode(true);

        width = getWidth();
        height = getHeight();

        g = getGraphics();

        musicPlayer = new MusicPlayer(0);
        musicIsActive = true;


        leftOption = "Back";
        rightOption = "Exit";

        menuOptions = new String[]{"Start Game", "Options", "Instructions", "Credits", "High Score", "Exit"};
        carMenuOptions = new String[]{"S Racer", "M Racer", "SM Racer"};

        menu = new Menu(leftOption, rightOption, menuOptions, highScorePoints, highScoreNames);
    }

    public void start() {

        clearScreen();

        menu.drawActiveMenu(this, g, currentlySelectedIndex);
        if (musicIsActive) {
            musicPlayer.startMusicPlayer();
        }
    }
    // Nokia softkeys: -6 and -7 values are OK on Nokia phones
    private int LEFT_SOFTKEY_CODE = -6;
    //private int RIGHT_SOFTKEY_CODE = -7;

    protected void keyPressed(int keyCode) {
        // work with menu according to its current state

        if (menuIsActive) { // draw active menu if menuIsActive is true

            keyCode = getGameAction(keyCode);
            if (keyCode == UP) {

                currentlySelectedIndex--;

                if (currentlySelectedIndex < 0) {

                    currentlySelectedIndex = menuOptions.length - 1;
                }

                //clearScreen();
                menu.drawActiveMenu(this, g, currentlySelectedIndex); // repaint active menu

            } else if (keyCode == DOWN) {

                currentlySelectedIndex++;

                if (currentlySelectedIndex >= menuOptions.length) {

                    currentlySelectedIndex = 0;
                }

                menu.drawActiveMenu(this, g, currentlySelectedIndex); // repaint active menu
            } else if (keyCode == FIRE) {

                clearScreen();
                g.setColor(0xFFFFFF); // black
                //clearScreen();

                if (currentlySelectedIndex == 2 || currentlySelectedIndex == 3 || currentlySelectedIndex == 4) {

                    menu.drawMenuItems(this, g, currentlySelectedIndex);
                    menu.drawInactiveMenu(this, g);
                    menuIsActive = false;
                    carMenuIsActive = false;
                    inactiveMenuIsActive = true;
                    optionsMenuIsActive = false;
                } else if (currentlySelectedIndex == 1) {
                    menu.drawOptionsMenu(this, g, 2);
                    optionsMenuIsActive = true;
                    menuIsActive = false;
                    carMenuIsActive = false;
                    inactiveMenuIsActive = false;
                } else if (currentlySelectedIndex == 0) {
                    menu.drawSelectCarMenu(this, g, carSelectedIndex);
                    menuIsActive = false;
                    carMenuIsActive = true;
                    inactiveMenuIsActive = false;
                    optionsMenuIsActive = false;

                } else if (currentlySelectedIndex == 5) {
                    menu.drawYesNoMenu(this, g);
                    menuIsActive = false;
                    carMenuIsActive = false;
                    inactiveMenuIsActive = false;
                    optionsMenuIsActive = false;
                    yesNoMenuIsActive = true;
                }
            }
        } else if (inactiveMenuIsActive) { // draw inactive menu

            // check if the "Options" or "Exit" buttons were pressed
            if (keyCode == LEFT_SOFTKEY_CODE) { // "Options" pressed

                clearScreen();

                menu.drawActiveMenu(this, g, currentlySelectedIndex); // activate menu

                menuIsActive = true;
                carMenuIsActive = false;
                inactiveMenuIsActive = false;
                optionsMenuIsActive = false;
            }

        } else if (carMenuIsActive) {

            if (keyCode == LEFT_SOFTKEY_CODE) {
                clearScreen();

                menu.drawActiveMenu(this, g, currentlySelectedIndex); // activate menu

                menuIsActive = true;
                carMenuIsActive = false;
                inactiveMenuIsActive = false;
                optionsMenuIsActive = false;
            } else {

                keyCode = getGameAction(keyCode);

                if (keyCode == LEFT) {
                    carSelectedIndex = 1;
                    menu.drawSelectCarMenu(this, g, carSelectedIndex);

                } else if (keyCode == RIGHT) {
                    carSelectedIndex = 2;
                    menu.drawSelectCarMenu(this, g, carSelectedIndex);

                } else if (keyCode == DOWN) {
                    carSelectedIndex = 0;
                    menu.drawSelectCarMenu(this, g, carSelectedIndex);

                } else if (keyCode == FIRE) {
                    if (keyIsPressed) {
                        musicPlayer.stopMusicPlayer();
                        menu.drawLetsGoOnScreen(this, g);
                        midlet.changeScreenToGame(carSelectedIndex, musicIsActive);
                    }
                    keyIsPressed = false;
                }
            }
        } else if (optionsMenuIsActive) {

            if (keyCode == LEFT_SOFTKEY_CODE) {
                clearScreen();

                menu.drawActiveMenu(this, g, currentlySelectedIndex); // activate menu

                menuIsActive = true;
                carMenuIsActive = false;
                inactiveMenuIsActive = false;
                optionsMenuIsActive = false;

            } else {

                keyCode = getGameAction(keyCode);
                if (keyCode == GAME_A) {
                    musicPlayer.startMusicPlayer();
                    musicIsActive = true;
                    menu.drawOptionsMenu(this, g, 1);
                } else if (keyCode == GAME_B) {
                    musicPlayer.stopMusicPlayer();
                    musicIsActive = false;
                    menu.drawOptionsMenu(this, g, 0);
                }
            }

        } else if (yesNoMenuIsActive) {

            keyCode = getGameAction(keyCode);
            if (keyCode == GAME_C) {
                exitGUI();
            } else if (keyCode == GAME_D) {

                menu.drawActiveMenu(this, g, currentlySelectedIndex);
                menuIsActive = true;
                carMenuIsActive = false;
                inactiveMenuIsActive = false;
                optionsMenuIsActive = false;
                yesNoMenuIsActive = false;
            }
        }
    }

    public void exitGUI() {
        midlet.destroyApp(false);
        midlet.notifyDestroyed();
    }

    public void clearScreen() {
        g.setColor(0x000000); // white
        g.fillRect(0, 0, width, height);
        flushGraphics();
    }

    public Menu returnMenu() {
        return this.menu;
    }

    public Graphics returnGraphics() {
        return this.g;
    }

    public void setItemsToNull() {
        if(musicPlayer!=null){
            musicPlayer.stopMusicPlayer();
        }
        this.musicPlayer.terminate();
        this.menu = null;

    }

    public void continuar() {
        clearScreen();
        menu.drawActiveMenu(this, g, currentlySelectedIndex);
    }

    public void setMusicIsActive(boolean musicIsActive) {
        this.musicIsActive = musicIsActive;
    }
}

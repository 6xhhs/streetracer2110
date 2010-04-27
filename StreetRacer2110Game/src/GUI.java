
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.*;

/**
 * Se encarga de manejar la interfas del usuario. Manejara los menus
 * de selección de autos, el menú principal, el menú activo, así como el
 * inactivo, el menú de opciones, y la música del previa al juego.
 * @author Salvador Aguilar Galindo, Manuel González Solano
 * @version 1.0, Abril 2010
 */
public class GUI extends GameCanvas {

    private StreetRacer2110 midlet;
    private Graphics g;
    private int width = 0;
    private int height = 0;
    private Menu menu;
    private String[] menuOptions;
    private int currentlySelectedIndex = 0;
    private boolean menuIsActive = true;
    private boolean inactiveMenuIsActive = false;
    private boolean carMenuIsActive = false;
    private boolean optionsMenuIsActive = false;
    private boolean yesNoMenuIsActive = false;
    private boolean helpMenuIsActive = false;
    private boolean keyIsPressed = true;
    private String[] carMenuOptions;
    private int carSelectedIndex = 0;
    private MusicPlayer musicPlayer;
    private boolean musicIsActive;

    /**
     *Constructor, crea una nueva interfaz para la navegación del menú principal
     * incluyendo a los valores de high score.
     * @param midlet Sera el midlet del juego
     * @param highScorePoints Es el vector de los mejores puntajes.
     * @param highScoreNames Es el vector de los mejores jugadores.
     */
    public GUI(StreetRacer2110 midlet, Vector highScorePoints, Vector highScoreNames) {

        super(false);

        this.midlet = midlet;
        setFullScreenMode(true);

        width = getWidth();
        height = getHeight();

        g = getGraphics();

        musicPlayer = new MusicPlayer(0);
        musicIsActive = true;

        menuOptions = new String[]{"The Streets", "Sound", "Help", "Credits", "Ranking", "Go Home"};
        carMenuOptions = new String[]{"S Racer", "M Racer", "SM Racer"};

        menu = new Menu(menuOptions, highScorePoints, highScoreNames);
    }

    /**
     *  Limpia la pantalla, dibuja el menú activo, y activa la música correspondiente
     *  al menú
     */
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

    /**
     * Se encarga de revisar que opción se selecciono, para saber que hacer, de
     * tal manera, que sabra que menú enseñar, a donde regresar, o que debe de hacer.
     * @param keyCode La variable, que indica que opción es la elegida
     */
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

                if (currentlySelectedIndex == 2) {
                    menu.drawHelpMenu(this, g, 2);
                    menuIsActive = false;
                    helpMenuIsActive = true;

                } else if (currentlySelectedIndex == 3 || currentlySelectedIndex == 4) {

                    menu.drawMenuItems(this, g, currentlySelectedIndex);
                    menuIsActive = false;
                    inactiveMenuIsActive = true;
                } else if (currentlySelectedIndex == 1) {
                    menu.drawOptionsMenu(this, g, 2);
                    optionsMenuIsActive = true;
                    menuIsActive = false;
                } else if (currentlySelectedIndex == 0) {
                    menu.drawSelectCarMenu(this, g, carSelectedIndex);
                    menuIsActive = false;
                    carMenuIsActive = true;

                } else if (currentlySelectedIndex == 5) {
                    menu.drawYesNoMenu(this, g);
                    menuIsActive = false;
                    yesNoMenuIsActive = true;
                }
            }
        } else if (inactiveMenuIsActive) { // draw inactive menu

            // check if the "Options" or "Exit" buttons were pressed
            if (keyCode == LEFT_SOFTKEY_CODE) { // "Options" pressed

                clearScreen();

                menu.drawActiveMenu(this, g, currentlySelectedIndex); // activate menu
                menuIsActive = true;
                inactiveMenuIsActive = false;
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

        } else if (carMenuIsActive) {

            if (keyCode == LEFT_SOFTKEY_CODE) {
                clearScreen();

                menu.drawActiveMenu(this, g, currentlySelectedIndex); // activate menu
                menuIsActive = true;
                carMenuIsActive = false;
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
        } else if (helpMenuIsActive) {

            if (keyCode == LEFT_SOFTKEY_CODE) {
                clearScreen();

                menu.drawActiveMenu(this, g, currentlySelectedIndex); // activate menu
                menuIsActive = true;
                helpMenuIsActive = false;

            } else {

                keyCode = getGameAction(keyCode);
                if (keyCode == LEFT) {
                    menu.drawHelpMenu(this, g, 2);
                } else if (keyCode == RIGHT) {
                    menu.drawHelpMenu(this, g, 1);
                }
            }

        } else if (yesNoMenuIsActive) {

            keyCode = getGameAction(keyCode);
            if (keyCode == GAME_C) {
                exitGUI();
            } else if (keyCode == GAME_D) {

                menu.drawActiveMenu(this, g, currentlySelectedIndex);
                menuIsActive = true;
                yesNoMenuIsActive = false;
            }
        }
    }

    /**
     *  Declara la opcion booleana destroyAPP falsa, para depués salir del GUI
     */
    public void exitGUI() {
        midlet.destroyApp(false);
        midlet.notifyDestroyed();
    }

    public void clearScreen() {
        g.setColor(0x000000); // white
        g.fillRect(0, 0, width, height);
        flushGraphics();
    }

    /**
     *
     * @return El menú seleccinado en el momento.
     */
    public Menu returnMenu() {
        return this.menu;
    }

    /**
     *
     * @return El canvas actual.
     */
    public Graphics returnGraphics() {
        return this.g;
    }

    /**
     * Coloca a musicPlayer y menu en null, eliminando a tales objetos.
     */
    public void setItemsToNull() {
        if (musicPlayer != null) {
            musicPlayer.stopMusicPlayer();
        }
        this.musicPlayer.terminate();
        this.menu = null;

    }

    /**
     * Limpia la pantalla, y depues dibuja el menu activo correspondiente.
     */
    public void continuar() {
        clearScreen();
        menu.drawActiveMenu(this, g, currentlySelectedIndex);
    }

    /**
     * Asigna el valor verdadero o falso de musicIsActive
     * @param musicIsActive variable booleana que indica si esta o no activa la música
     */
    public void setMusicIsActive(boolean musicIsActive) {
        this.musicIsActive = musicIsActive;
    }
}


import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
/*
 * Se encarga de dibujar los menus previos a que inicie juego, las imagenes
 * de los menus, sería el menú principal. (el menú de pausa se maneja en juego)
 * @author Salvador Aguilar Galindo, Manuel González Solano
 * @version 1.0, Abril 2010
 *
 */

public class Menu {

    private Image mainMenu = null;
    private Image credsBckgrnd;
    private Image instBckgrnd;
    private Image histBckgrnd;
    private String[] menuOptions;
    private Graphics g;
    private int padding = 5;
    private int selectedIndex;
    private Image sRacer = null;
    private Image mRacer = null;
    private Image sMRacer = null;
    private Image carSlctBckgrnd = null;
    private Image sRacerInfo, mRacerInfo, sMRacerInfo;
    private Image speakerOn, speakerOff;
    private Image rankingImg;
    private String soundActive = "Sound Active?";
    private String yesNoToSound = "A: Yes    B: No";
    private String letsGo ="Let's Go!";
    private String shutdown = "SHUT DOWN?";
    private String shYes ="C: YES";
    private String shNo="D: NO";
    private Vector hiScorePts, hiScoreNms;
    private static final int BLACK = 0x000000;
    private static final int RED = 0xff0000;
    private static final int WHITE = 0xffffff;

    /**
     * Constructor, crea un nuevo menú con las imágenes necesarias, los valores de high score y las opciones del menú principal
     * @param leftOption String que indica que se selecciono la opción izquierda.
     * @param rightOption String que indica que se selecciono la opción derecha.
     * @param menuOptions Arreglo que muestra las opciones del menú.
     * @param hiScorePts Vector que maneja los mejores puntajes.
     * @param hiScoreNms Vector que maneja los nombres de los mejores puntajes.
     */
    public Menu(String[] menuOptions, Vector highScorePoints, Vector highScoreNames) {

//        this.leftOption = leftOption;
        this.menuOptions = menuOptions;
        this.hiScorePts = highScorePoints;
        this.hiScoreNms = highScoreNames;

        try {
            sRacer = Image.createImage("/SRacerMenu.jpg");
            sRacerInfo = Image.createImage("/SRacerInfo.png");

            mRacer = Image.createImage("/MRacerMenu.jpg");
            mRacerInfo = Image.createImage("/MRacerInfo.png");

            sMRacer = Image.createImage("/SMRacerMenu.jpg");
            sMRacerInfo = Image.createImage("/SMRacerInfo.png");

            mainMenu = Image.createImage("/main menu.jpg");
            carSlctBckgrnd = Image.createImage("/car select menu.jpg");
            credsBckgrnd = Image.createImage("/menuBckgrnd.jpg");
            instBckgrnd = Image.createImage("/InstMenu.jpg");
            histBckgrnd = Image.createImage("/HistMenu.jpg");

            speakerOn = Image.createImage("/speaker on.png");
            speakerOff = Image.createImage("/speaker off.png");

            rankingImg = Image.createImage("/ranking.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Dibuja el menú principal, su posición y la de los elementos.
     * @param canvas indicara donde se dibuajara el menú.
     * @param graphics de la pantalla actual
     * @param selectedOptionIndex indicará qué opción está seleccionada.
     */
    public void drawActiveMenu(GameCanvas canvas, Graphics graphics, int selectedOptionIndex) {
        this.selectedIndex = selectedOptionIndex;
        this.g = graphics;
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        int fontHeight = font.getHeight();

        // clear menu bar background

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        g.drawImage(mainMenu, 0, 0, g.TOP | g.LEFT);
        // draw default menu bar options

        g.setFont(font);
        g.setColor(BLACK); // black

        canvas.flushGraphics();
        // draw menu options

        if (menuOptions != null) {

            // check out the max width of a menu (for the specified menu font)

            int menuMaxWidth = 0;
            int menuMaxHeight = 0;
            int currentWidth = 0;

            //check each option and find the maximal width

            for (int i = 0; i < menuOptions.length; i++) {

                currentWidth = font.stringWidth(menuOptions[i]);

                if (currentWidth > menuMaxWidth) {
                    menuMaxWidth = currentWidth; // update
                }

                menuMaxHeight += fontHeight + padding; // for a current menu option

            } // end for each menu option

            menuMaxWidth += 2 * padding; // padding from left and right

            // draw menu options (from up to bottom)
            g.setFont(font);
            int menuOptionX = 0;
            int menuOptionY = 0;
            if (width >= 360) {
                menuOptionX = (width - (menuMaxWidth + (width / 14))) / 2;
                menuOptionY = (height - menuMaxHeight) / 2;
            } else {
                menuOptionX = (360 - (menuMaxWidth + (width / 12))) / 2;
                menuOptionY = (360 - menuMaxHeight) / 2;
            }

            for (int i = 0; i < menuOptions.length; i++) {

                if (i != selectedIndex) { // draw unselected menu option
                    g.setColor(WHITE); // WHITE
                } // end if draw unselected menu option
                else { // draw selected menu option
                    g.setColor(RED); // red
                }

                g.drawString(menuOptions[i], menuOptionX, menuOptionY, g.TOP | g.LEFT);
                menuOptionY += padding + fontHeight;
            }

            canvas.flushGraphics();
        }
    }

    /**
     * Dibuja los elementos de los menus, dependiendo de cual menú
     * esté seleccionado
     * @param canvas canvas del juego
     * @param graphics de la pantalla actual
     * @param i indicara qué menú esta seleccionado.
     */
    public void drawMenuItems(GameCanvas canvas, Graphics graphics, int i) {

        this.g = graphics;
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        int fontHeight = font.getHeight();
        int width = canvas.getWidth();

        g.setFont(font);
        g.setColor(BLACK);
        if (i == 3) {
            g.drawImage(credsBckgrnd, 0, 0, g.TOP | g.LEFT);
            g.drawString("Autores", width/3, 0, g.TOP | g.LEFT);
            g.drawString("Manuel Gonzalez Solano", 45, 2*(fontHeight + 5), g.TOP | g.LEFT);
            g.drawString("A01165461@itesm.mx",45, 3*(fontHeight+5), g.TOP|g.LEFT);
            g.drawString("Carrera: ISC09", 45, 4*(fontHeight + 5), g.TOP | g.LEFT);
            g.drawString("Salvador Aguilar Galindo", 45, 6*(fontHeight + 5), g.TOP | g.LEFT);
            g.drawString("A00967057@itesm.mx",45, 7*(fontHeight+5), g.TOP|g.LEFT);
            g.drawString("Carrera: ISC09", 45, 8*(fontHeight + 5), g.TOP | g.LEFT);
            
        } else if (i == 4) {
            int highScoreWordHeight = 2*(fontHeight + 5);
            g.drawImage(credsBckgrnd, 0, 0, g.TOP | g.LEFT);
            g.drawString("Top Rank", width/3, 0, g.TOP | g.LEFT);
            g.drawImage(rankingImg, 203, 51, g.TOP|g.LEFT);

            hiScorePts.setSize(5);
            hiScoreNms.setSize(5);

            for (int e = 0; e < hiScorePts.size(); e++) {
                g.drawString(hiScoreNms.elementAt(e) + " " + hiScorePts.elementAt(e), 45, highScoreWordHeight, g.TOP | g.LEFT);
                highScoreWordHeight += (fontHeight + 5);
            }
            highScoreWordHeight = fontHeight + 5;
        }
        canvas.flushGraphics();
    }

    /**
     *
     * @return graphics de la pantalla actual
     */
    public Graphics returnGraphics() {
        return this.g;
    }

    /**
     * actualiza al índice que indica qué opción del menú principal está seleccionada.
     * @param updatedIndex el índice nuevo de las opciones del menú principal
     */
    public void updateActualizedSelectedIndex(int updatedIndex) {
        this.selectedIndex = updatedIndex;
    }

    /**
     *
     * @return el índice de las opciones del menú principal
     */
    public int returnActualizedSelectedIndex() {
        return this.selectedIndex;
    }

    /**
     * actualiza al menú principal
     * @param graphics graphics nuevo para desplegar en pantalla
     * @param selectedOptionIndex índice de las opciones del menú principal
     */
    public void updateActiveMenu(Graphics graphics, int selectedOptionIndex) {
        this.g = graphics;
        this.selectedIndex = selectedOptionIndex;
    }

    /**
     * dibuja al menú de selección de auto segun el auto seleccionado.
     * @param canvas Canvas de juego
     * @param g graphics de la pantalla actual
     * @param i índice de las opciones de autos que se pueden seleccionar
     */
    public void drawSelectCarMenu(GameCanvas canvas, Graphics g, int i) {
        this.g = g;
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);

        g.drawImage(carSlctBckgrnd, 0, 0, g.TOP | g.LEFT);
        canvas.flushGraphics();

        g.setColor(WHITE);   //white
        if (i == 0) {
            g.drawImage(sRacer, 117, 234, g.TOP | g.LEFT);
            g.drawImage(sRacerInfo, 80, 90, g.TOP | g.LEFT);
        } else if (i == 1) {
            g.drawImage(mRacer, 3, 184, g.TOP | g.LEFT);
            g.drawImage(mRacerInfo, 80, 90, g.TOP | g.LEFT);
        } else if (i == 2) {
            g.drawImage(sMRacer, 258, 197, g.TOP | g.LEFT);
            g.drawImage(sMRacerInfo, 80, 90, g.TOP | g.LEFT);
        }

        g.setFont(font);
        //g.setColor(0xff6600); // black

        canvas.flushGraphics();
    }

    /**
     * dibuja a los contenidos de la opción de Options del menú principal, tomando
     * en cuenta la selección del estado del audio.
     * @param canvas canvas del juego
     * @param g graphics de la pantalla actual
     * @param i índice del estado del audio
     */
    public void drawOptionsMenu(GameCanvas canvas, Graphics g, int i) {
        this.g = g;
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        g.drawImage(credsBckgrnd, 0, 0, g.TOP | g.LEFT);

        g.setFont(font);
        g.setColor(BLACK);

        g.drawString(soundActive,width/3, padding, g.TOP | g.LEFT);
        g.drawString(yesNoToSound, width/3, height - padding, g.LEFT | g.BOTTOM);
        g.drawImage(speakerOn, 135, (height / 3), g.TOP | g.LEFT);

        if (i == 1) {
            g.drawImage(speakerOn, 135, (height / 3), g.TOP | g.LEFT);
        } else if (i == 0) {
            g.drawImage(speakerOff, 135, (height / 3), g.TOP | g.LEFT);
        }

        canvas.flushGraphics();
    }

    public void drawHelpMenu(GameCanvas canvas, Graphics g, int i) {
        if (i == 1) {
            g.drawImage(histBckgrnd, 0, 0, g.TOP | g.LEFT);
        } else if (i == 2) {
            g.drawImage(instBckgrnd, 0, 0, g.TOP | g.LEFT);
        }
        canvas.flushGraphics();
    }

    /**
     * al seleccionar el auto deseado e iniciar el juego, se encarga de dibujar
     * el letrero de "Let's Go!" en pantalla
     * @param canvas canvas del juego
     * @param g graphics de la pantalla actual
     */
    public void drawLetsGoOnScreen(GameCanvas canvas, Graphics g) {
        this.g = g;
        int width = canvas.getWidth();
        g.setColor(WHITE);   //white
        g.drawString(letsGo, (width/3 ), 5, g.TOP | g.LEFT);
        canvas.flushGraphics();
    }

    /**
     * al seleccionar la opción de Exit del menú principal, se encarga de dibujar el submenú
     * di si o no para confirmar la salida del juego
     * @param canvas canvas del juego
     * @param g graphics de la pantalla actual
     */
    public void drawYesNoMenu(GameCanvas canvas, Graphics g) {

        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        g.setFont(font);
        g.drawImage(mainMenu, 0, 0, g.TOP | g.LEFT);
        g.setColor(WHITE);
        g.drawString(shutdown, 100, 100, g.TOP | g.LEFT);
        g.drawString(shYes, 120, 130, g.TOP | g.LEFT);
        g.drawString(shNo, 120, 160, g.TOP | g.LEFT);
        canvas.flushGraphics();
    }
}


import java.io.IOException;
import java.util.*;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class Menu {

    private Image mainMenu = null;
    private Image creditsBackground;
    private Image instructionsBackground;
    private String leftOption; // will be displayed when menu is inactive
    private String okOption = "Ready!";
    private String[] menuOptions;
    private Graphics g;
    private int padding = 5;
    private int selectedIndex;
    private Image sRacer = null;
    private Image mRacer = null;
    private Image sMRacer = null;
    private Image carSelectBackground = null;
    private Image sRacerInfo, mRacerInfo, sMRacerInfo;

    public Menu(String leftOption, String rightOption, String[] menuOptions) {

        this.leftOption = leftOption;
        this.menuOptions = menuOptions;


        try {
            sRacer = Image.createImage("/SRacerMenu.jpg");
            sRacerInfo = Image.createImage("/SRacerInfo.png");

            mRacer = Image.createImage("/MRacerMenu.jpg");
            mRacerInfo = Image.createImage("/MRacerInfo.png");

            sMRacer = Image.createImage("/SMRacerMenu.jpg");
            sMRacerInfo = Image.createImage("/SMRacerInfo.png");

            mainMenu = Image.createImage("/main menu.jpg");
            carSelectBackground = Image.createImage("/car select menu.jpg");
            creditsBackground = Image.createImage("/credits.jpg");
            instructionsBackground = Image.createImage("/InstructionsMenuBackground.jpg");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void drawInactiveMenu(GameCanvas canvas, Graphics graphics) {

        // create inactive menu font
        this.g = graphics;
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);

        // clear inactive menu background

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        g.setColor(0xffff33);

        // draw left and right menu options

        g.setFont(font);
        //g.setColor(0xffffff); // black

        //g.drawString(leftOption, padding, height - padding, g.LEFT | g.BOTTOM);
        canvas.flushGraphics();
    }

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
        g.setColor(0x000000); // black

        canvas.flushGraphics();
        // draw menu options

        if (menuOptions != null) {

            // check out the max width of a menu (for the specified menu font)

            int menuMaxWidth = 0;
            int menuMaxHeight = 0;
            int currentWidth = 0;

            // we'll simply check each option and find the maximal width

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

                    g.setColor(0xffffff); // black

                } // end if draw unselected menu option
                else { // draw selected menu option

                    g.setColor(0xff6600); // orange
                }

                g.drawString(menuOptions[i], menuOptionX, menuOptionY, g.TOP | g.LEFT);
                menuOptionY += padding + fontHeight;
            }

            canvas.flushGraphics();
        }
    }

    public void drawMenuItems(GameCanvas canvas, Graphics graphics, int i) {

        this.g = graphics;
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        int fontHeight = font.getHeight();

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        g.setFont(font);
        g.setColor(0xff6600); // orange

        if (i == 0) {
        } else if (i == 1) {
            g.drawImage(instructionsBackground, 0, 0, g.TOP | g.LEFT);
            g.drawString("Options", 0, 0, g.TOP | g.LEFT);
            g.drawString("Here are where the", 0, fontHeight + 5, g.TOP | g.LEFT);
            g.drawString("options go.", 0, 2 * (fontHeight + 5), g.TOP | g.LEFT);
        } else if (i == 2) {

            g.drawImage(instructionsBackground, 0, 0, g.TOP | g.LEFT);
            
        } else if (i == 3) {
            g.drawImage(creditsBackground, 0, 0, g.TOP | g.LEFT);
            g.drawString("Autores", 0, 0, g.TOP | g.LEFT);
            g.drawString("Manuel Gonzalez Solano 1165461", 0, fontHeight + 5, g.TOP | g.LEFT);
            g.drawString("Salvador Aguilar Galindo 967057", 0, 2 * (fontHeight + 5), g.TOP | g.LEFT);
            g.drawString("Carrera: ISC09",0,3*(fontHeight+5),g.TOP|g.LEFT);
        } else if (i == 4) {
            g.drawImage(creditsBackground, 0, 0, g.TOP | g.LEFT);
            g.drawString("High Scores", 0, 0, g.TOP | g.LEFT);
            g.drawString("Man: 252009 pts", 0, fontHeight + 5, g.TOP | g.LEFT);
            g.drawString("Sal: 30943 pts", 0, 2 * (fontHeight + 5), g.TOP | g.LEFT);
        }
        canvas.flushGraphics();
    }

    public Graphics returnGraphics() {
        return this.g;
    }

    public void updateActualizedSelectedIndex(int updatedIndex) {
        this.selectedIndex = updatedIndex;
    }

    public int returnActualizedSelectedIndex() {
        return this.selectedIndex;
    }

    public void updateActiveMenu(Graphics graphics, int selectedOptionIndex) {
        this.g = graphics;
        this.selectedIndex = selectedOptionIndex;

    }

    public void drawSelectCarMenu(GameCanvas canvas, Graphics g, int i) {
        this.g = g;
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        int fontHeight = font.getHeight();

        int width = canvas.getWidth();
        int height = canvas.getHeight();


        g.drawImage(carSelectBackground, 0, 0, g.TOP | g.LEFT);
        canvas.flushGraphics();

        g.setColor(0xff6600);
        if (i == 0) {
            //g.drawString("S Racer", 102,243, g.TOP|g.LEFT);
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
        g.setColor(0xff6600); // black

        g.drawString("Pick Your Ride", 50, 0, g.TOP | g.LEFT);
        g.drawString(leftOption, padding, height - padding, g.LEFT | g.BOTTOM);
        g.drawString(okOption, ((width / 2) + 25), height - padding, g.RIGHT | g.BOTTOM);

        canvas.flushGraphics();
    }

    public void drawOptionsMenu(GameCanvas canvas, Graphics g, int i) {
        this.g = g;
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        int fontHeight = font.getHeight();

        int width = canvas.getWidth();
        int height = canvas.getHeight();


        g.drawImage(creditsBackground, 0, 0, g.TOP | g.LEFT);
        //canvas.flushGraphics();

        g.setColor(0x000000);   //black
        g.fillRect(0, 330, width, fontHeight + 10);

        g.setFont(font);
        g.setColor(0xff6600); // orange

        g.drawString("Sound Active?", (width / 2), padding, g.TOP | g.LEFT);
        g.drawString("A: Yes    B: No", padding, height - padding, g.LEFT | g.BOTTOM);
        //g.drawString("D: Back", width, height - padding, g.RIGHT | g.BOTTOM);

        if(i==1)
            g.drawString("Sound On", (width / 2), (height / 2), g.TOP | g.LEFT);
        else if(i==0)
            g.drawString("Sound Off", (width / 2), (height / 2), g.TOP | g.LEFT);

        canvas.flushGraphics();
    }

    public void drawLetsGoOnScreen(GameCanvas canvas, Graphics g) {
        this.g = g;
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        g.setColor(0xff6600);
        g.drawString("Let's Go!", (width / 2), (height / 2), g.TOP | g.LEFT);
        canvas.flushGraphics();
    }

    public void drawYesNoMenu(GameCanvas canvas, Graphics g){

        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        g.setFont(font);
        g.drawImage(mainMenu,0,0,g.TOP|g.LEFT);
        g.setColor(0xff6600);
        g.drawString("Shut Down?", 100, 100, g.TOP | g.LEFT);
        g.drawString("C: YES",120,130,g.TOP|g.LEFT);
        g.drawString("D: NO",120,160,g.TOP|g.LEFT);
        canvas.flushGraphics();
    }
}

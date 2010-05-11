
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Ramp {

    private Image rampImage;
    private int currentLevel;
    private int rampX;
    private int rampY;
    private int holeX;
    private int holeY;
    private final int INC_X;
    private int incY = -5;
    private int rampHeight;
    private int rampWidth;
    private final int WIDTH, HEIGHT;
    private Vector holes;
    private int holeImg;

    public Ramp(int currentLevel, int screenWidth, int screenHeight) {

        WIDTH = screenWidth;
        HEIGHT = screenHeight;
        this.currentLevel = currentLevel;
        holes = new Vector();
        rampImage = null;
        holeImg = 0;
        if (currentLevel == 1) {
            try {

                rampImage = Image.createImage("/level 3 ramp.png");
                holes.addElement(Image.createImage("/fire hole 1.png"));
                holes.addElement(Image.createImage("/fire hole 2.png"));
                rampHeight = 73;
                rampWidth = 146;
                holeX = (screenWidth * 5);
                holeY = screenHeight - 134;
                rampX = (screenWidth * 5);
                rampY = screenHeight - rampHeight;
            } catch (IOException ex) {
            }
        } else if (currentLevel == 2) {
            try {

                rampImage = Image.createImage("/ramp level 2.png");

                holes.addElement(Image.createImage("/fire hole 1.png"));
                holes.addElement(Image.createImage("/fire hole 2.png"));
                rampHeight = 65;
                rampWidth = 131;
                holeX = (screenWidth * 5);
                holeY = screenHeight - 134;
                rampX = (screenWidth * 5);
                rampY = screenHeight - rampHeight;
            } catch (IOException ex) {
            }
        } else if (currentLevel == 3) {
            try {
                rampImage = Image.createImage("/ramp level 3.png");

                holes.addElement(Image.createImage("/fire hole 1.png"));
                holes.addElement(Image.createImage("/fire hole 2.png"));
                rampHeight = 59;
                rampWidth = 116;
                holeX = (screenWidth * 5);
                holeY = screenHeight - 134;
                rampX = (screenWidth * 5);
                rampY = screenHeight - rampHeight;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        INC_X = 15 + this.currentLevel;
    }

    public void dibujar(Graphics g) {

        g.drawImage((Image) holes.elementAt(holeImg), holeX, holeY, g.TOP | g.LEFT);
        g.drawImage(rampImage, rampX, rampY, g.TOP | g.LEFT);



    }

    public void actualizar() {
        if (this.rampX >= -this.rampWidth) {
            this.rampX -= INC_X;
        }

        if (this.holeX >= -265) {
            this.holeX -= INC_X;
            updateRampY();
        }

        if (holeImg == 0) {
            holeImg = 1;
        } else {
            holeImg = 0;
        }

    }

    public int getRampX() {
        return this.rampX;
    }

    public int getRampY() {
        return this.rampY;
    }

    public int getRampWidth() {
        return this.rampWidth;
    }

    public int getRampHeight() {
        return this.rampHeight;
    }

    public void resetRampCoordinates() {
        this.rampX = (WIDTH * 5);
        this.rampY = HEIGHT - rampHeight;

        holeX = (WIDTH * 5);
        holeY = HEIGHT - 134;
    }

    private void updateRampY() {
        if (rampY < (HEIGHT - 134)) {
            rampY = (HEIGHT - 134);
            incY = -incY;
        }

        if ((rampY + rampHeight) > HEIGHT) {
            rampY = HEIGHT - rampHeight;
            incY = -incY;
        }

        rampY += incY;

    }
}

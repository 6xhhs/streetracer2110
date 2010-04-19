




import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Ramp {

    private Image rampImage;
    private int rampX;
    private int rampY;
    private static final int INC_X = -15;
    private int rampHeight;
    private int rampWidth;

    private final int ANCHO, ALTO;

    public Ramp(int currentLevel, int screenWidth, int screenHeight) {

        ANCHO = screenWidth;
        ALTO = screenHeight;

        rampImage = null;
        if (currentLevel == 1) {
            try {
                rampImage = Image.createImage("/ramp.png");
                rampHeight = 163;
                rampWidth = 195;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (currentLevel == 2) {
            try {
                rampImage = Image.createImage("/ramp.png");
                rampHeight = 163;
                rampWidth = 195;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (currentLevel == 3) {
            try {
                rampImage = Image.createImage("/ramp.png");
                rampHeight = 163;
                rampWidth = 195;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        rampX = screenWidth*2;
        rampY = screenHeight - rampHeight;
    }

    public void dibujar(Graphics g) {
        g.drawImage(rampImage, rampX, rampY, g.TOP | g.LEFT);
    }

    public void actualizar() {
        if(this.rampX >= -this.rampWidth)
            this.rampX -= 15;
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
        this.rampX = ANCHO;
        this.rampY = ALTO-rampHeight;
    }
}

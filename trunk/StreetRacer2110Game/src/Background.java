
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Background {
    private static final int LEVEL_X_LIMIT = -1100;

    private Image backgroundImage;
    private Image backgroundImage2;
    private int x;
    private int x2;
    private int alto;
    private int ancho;
    private boolean isSkyBackground;
    private int changeInX = 0;

    public Background(String archivo, boolean isSkyBackground, int speed) throws IOException {

        this.isSkyBackground = isSkyBackground;

        backgroundImage = Image.createImage(archivo);
        changeInX = speed;

        this.ancho = 360;
        this.alto = 360;

        x = 0;

        if (!this.isSkyBackground) {
            backgroundImage2 = backgroundImage;
            x2 = this.ancho;
        }

    }

    public void moveBackgroundImage() {

        if (isSkyBackground) {

            if (x <= LEVEL_X_LIMIT) {
                changeInX = 0;
            }
            x -= changeInX;

        } else {
            if (x <= -ancho) {
                x = ancho;
                x2 -= 0;
            } else {
                x -= changeInX;
                x2 -= changeInX;
            }

            if (x2 <= -ancho) {
                x2 = ancho;
                x -= 0;
            } else {
                x -= changeInX;
                x2 -= changeInX;
            }
        }
    }

    public void dibujar(Graphics g) {
        g.drawImage(backgroundImage, x, 0, g.TOP | g.LEFT);
        if (!isSkyBackground) {
            g.drawImage(backgroundImage2, x2, 0, g.TOP | g.LEFT);
        }
    }

    public void dibujar2(Graphics g) {
        g.drawImage(backgroundImage, x, alto - backgroundImage.getHeight(), g.TOP | g.LEFT);
        g.drawImage(backgroundImage2, x2, alto - backgroundImage.getHeight(), g.TOP | g.LEFT);
    }

    public Image returnImage() {
        return this.backgroundImage;
    }

    void resetValues() {
        x = 0;

        if (!this.isSkyBackground) {
            backgroundImage2 = backgroundImage;
            x2 = this.ancho;
        }
    }

    public int returnXValue(){
        return this.x;
    }
}


import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Health {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;
    private static final int X_CHANGE = 15;
    private int xPosition;
    private int yPosition;
    private boolean healthIsActive;
    private Image healthImg;

    public Health(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.healthIsActive = false;

        try {
            healthImg = Image.createImage("/pastor.png");
        } catch (IOException ex) {}
    }

    public void update() {
        this.xPosition -= X_CHANGE;
    }

    public void draw(Graphics g) {
        g.drawImage(healthImg, xPosition, yPosition, g.TOP | g.LEFT);
    }

    public boolean returnHealthIsActive() {
        return healthIsActive;
    }

    public void setHealthIsActive(boolean healthIsActive) {
        this.healthIsActive = healthIsActive;
    }

    public void resetHealthObj(int newXPos, int newYPos) {
        this.xPosition = newXPos;
        this.yPosition = newYPos;
        this.healthIsActive = false;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static int getWidth() {
        return WIDTH;
    }

    public int getXPos() {
        return xPosition;
    }

    public int getYPos() {
        return yPosition;
    }
}


import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Health {

    private static final int WIDTH = 40;
    private static final int HEIGHT = 65;
    private static final int X_CHANGE = 15;
    private int xPosition;
    private int yPosition;
    private boolean healthObtained;
    private boolean healthIsActive;
    private Image healthImg;

    public Health(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.healthObtained = false;
        this.healthIsActive = false;

        try {
            healthImg = Image.createImage("/pastor.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void update() {
        this.xPosition -= X_CHANGE;
    }

    public void draw(Graphics g) {
        g.drawImage(healthImg, xPosition, yPosition, g.TOP | g.LEFT);
    }

    public boolean returnHealthObtained() {
        return healthObtained;
    }

    public boolean returnHealthIsActive() {
        return healthIsActive;
    }

    public void setHealthObtained(boolean healthObtained) {
        this.healthObtained = healthObtained;
    }

    public void setHealthIsActive(boolean healthIsActive) {
        this.healthIsActive = healthIsActive;
    }

    public void resetHealthObj(int newXPos, int newYPos) {
        this.xPosition = newXPos;
        this.yPosition = newYPos;
        this.healthObtained = false;
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

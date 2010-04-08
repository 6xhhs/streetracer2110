
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;

public class SplashScreen extends GameCanvas {

    private Graphics g;
    private Image img;

    public SplashScreen(int index) {
        super(true);

        setFullScreenMode(true);

        g = getGraphics();

        img = null;
        if (index == 1) {
            try {
                img = Image.createImage("/itesm.jpg");
            } catch (IOException e) {
                System.out.println("Image not found");
            }
        } else if (index == 2) {
            try {
                img = Image.createImage("/Game Cover.jpg");
            } catch (IOException e) {
                System.out.println("Image not found");
            }
        } else if (index == 3) {
            try {
                img = Image.createImage("/Game Over Screen.jpg");
            } catch (IOException e) {
                System.out.println("Image not found");
            }
        } else if (index == 4) {
            try {
                img = Image.createImage("/Level 2 Load Screen.jpg");
            } catch (IOException e) {
                System.out.println("Image not found");
            }
        } else if (index == 5) {
            try {
                img = Image.createImage("/Level 3 Load Screen.jpg");
            } catch (IOException e) {
                System.out.println("Image not found");
            }
        } else if (index == 6) {
            try {
                img = Image.createImage("/End Of Game Load Screen.jpg");
            } catch (IOException e) {
                System.out.println("Image not found");
            }
        }
    }

    protected void paint() {

        g.setColor(0xFFFFFF);
        g.drawImage(img, 0, 0, g.TOP | g.LEFT);
    }
}

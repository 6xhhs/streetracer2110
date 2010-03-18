
import java.io.IOException;
import java.util.*;
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
                img = Image.createImage("/cover screen.jpg");
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

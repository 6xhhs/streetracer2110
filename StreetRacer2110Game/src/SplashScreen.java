
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;

public class SplashScreen extends GameCanvas {

    private Graphics g;
    private Image img;
    private int index;
    private int width;
    private int height;

    public SplashScreen(int index) {
        super(true);

        this.index = index;
        setFullScreenMode(true);

        width = this.getWidth();
        height = this.getHeight();

        g = getGraphics();

        img = null;

        try {
            img = Image.createImage("/splashscreen "+index+".jpg");
        } catch (IOException e) {
        }
    }

    protected void paint() {
        if (index == 1) {
            g.drawImage(img, width / 7, height / 4, g.TOP | g.LEFT);
        } else {
            g.drawImage(img, 0, 0, g.TOP | g.LEFT);
        }
        if (index == 2) {
            g.setColor(0xFFFFFF);
            g.drawString("Loading, Please Wait", width / 6, height - 50, g.TOP | g.LEFT);
        }
    }
}
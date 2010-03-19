
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class LoadScreen extends GameCanvas {

    private Image loadScreenImage;
    private Graphics g;

    public LoadScreen(int index) {
        super(true);
        this.g = getGraphics();
        if (index == 0) {
            try {
                //Load Game Over Screen
                loadScreenImage = Image.createImage("/Game Over Screen.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (index == 1) {
            //Load Level 1 Screen
            try {
                //Load Game Over Screen
                loadScreenImage = Image.createImage("/Level 1 Screen.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (index == 2) {
            //Load Level 2 Screen
            try {
                //Load Game Over Screen
                loadScreenImage = Image.createImage("/Level 2 Screen.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (index == 3) {
            //Load Level 3 Screen
            try {
                //Load Game Over Screen
                loadScreenImage = Image.createImage("/Level 3 Screen.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

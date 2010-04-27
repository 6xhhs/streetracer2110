
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
        if (index == 1) {
            try {
                img = Image.createImage("/tecLogo.jpg");
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
        if(index==1){
            g.drawImage(img,width/6, height/4,g.TOP|g.LEFT);
        }else{
            g.drawImage(img, 0, 0, g.TOP | g.LEFT);
        }
    }
}

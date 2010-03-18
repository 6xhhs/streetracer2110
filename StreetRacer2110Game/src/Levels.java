
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

public class Levels {

    private Background foreground;
    private Background midground;
    private Background clouds;
    private Background sky;
    private int level;

    public Levels(int level, int width, int height) {

        this.level = level;

        if (this.level == 1) {
            try {
                foreground = new Background("/highway2.jpg",false,9);
                midground = new Background("/foregroundBuildings2.png",false,3);
                sky = new Background("/SkyLevel1.jpg",false,1);
                clouds = new Background("/clouds2.png",false,1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else if (level == 2) {
            try {
                foreground = new Background("/highway2.jpg",false,9);
                midground = new Background("/foregroundBuildings2.png",false,3);
                sky = new Background("/SkyLevel1.jpg",true,1);
                clouds = new Background("/clouds2.png",false,1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (level == 3) {
            try {
                foreground = new Background("/highway2.jpg",false,9);
                midground = new Background("/foregroundBuildings2.png",false,3);
                sky = new Background("/SkyLevel1.jpg",false,1);
                clouds = new Background("/clouds2.png",false,1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void dibujar(Graphics g) {
        sky.dibujar(g);
        clouds.dibujar(g);
        midground.dibujar(g);
        foreground.dibujar2(g);
    }

    public void actualizar() {
        sky.moveBackgroundImage();
        clouds.moveBackgroundImage();
        midground.moveBackgroundImage();
        foreground.moveBackgroundImage();
    }
}
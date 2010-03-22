
import java.io.IOException;
import javax.microedition.lcdui.Graphics;

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
                sky = new Background("/SkyLevel1.jpg",true,1);
                clouds = new Background("/clouds2.png",false,1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else if (level == 2) {
            try {
                foreground = new Background("/foreground level 2.jpg",false,9);
                midground = new Background("/midground level 2.png",false,3);
                sky = new Background("/sky level 2.jpg",true,1);
                clouds = new Background("/clouds2.png",false,1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (level == 3) {
            try {
                foreground = new Background("/foreground level 3.jpg",false,9);
                midground = new Background("/midground level 3.png",false,3);
                sky = new Background("/sky level 3.jpg",true,1);
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

    void resetValues() {
        foreground.resetValues();
        midground.resetValues();
        sky.resetValues();
        clouds.resetValues();
    }

    public int returnSkyBackgroundXValue(){
        return sky.returnXValue();
    }
}

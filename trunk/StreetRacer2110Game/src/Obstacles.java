
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Obstacles {

    private int obstacleWidth=70;
    private int obstacleHeight=17;
    private int screenWidth;
    private int screenHeight;
    private Image obstacleImage;
    private boolean obstacleHasCollided;
    private int obstacleX;
    private int obstacleY;


    public Obstacles(int screenWidth, int screenHeight, int obstacleSelect) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.obstacleX=screenWidth;
        this.obstacleY=screenHeight;

        this.obstacleHasCollided=false;

        if (obstacleSelect == 0) {
            try {
                obstacleImage = Image.createImage("/hole.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (obstacleSelect == 1) {
            try {
                obstacleImage = Image.createImage("/hole.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void dibujar(Graphics g){
        g.drawImage(obstacleImage,obstacleX,obstacleY,g.TOP|g.LEFT);
    }

    public void actualizar(){
        this.obstacleX-=15;
    }

    public int getObstacleX(){
        return this.obstacleX;
    }

    public int getObstacleY(){
        return this.obstacleY;
    }

    public boolean obstacleHasCollided(){
        return this.obstacleHasCollided;
    }

    public int getObstacleWidth(){
        return this.obstacleWidth;
    }
}

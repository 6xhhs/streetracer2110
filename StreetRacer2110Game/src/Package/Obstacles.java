package Package;


import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Obstacles {

    private int obstacleWidth;
    private int obstacleHeight;
    private Image obstacleImage;
    private boolean obstacleHasCollided;
    private int obstacleX;
    private int obstacleY;

    public Obstacles(int obstacleX, int obstacleY, int obstacleSelect) {
        this.obstacleX = obstacleX;
        this.obstacleY = obstacleY;

        this.obstacleHasCollided = false;

        if (obstacleSelect == 0) {
            try {
                obstacleImage = Image.createImage("/hole.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            obstacleWidth = 55;
            obstacleHeight = 14;
        } else if (obstacleSelect == 1) {
            try {
                obstacleImage = Image.createImage("/hole.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            obstacleWidth = 55;
            obstacleHeight = 14;
        }
    }

    public void dibujar(Graphics g) {
        g.drawImage(obstacleImage, obstacleX, obstacleY, g.TOP | g.LEFT);
    }

    public void actualizar() {
        this.obstacleX -= 15;
    }

    public int getObstacleX() {
        return this.obstacleX;
    }

    public int getObstacleY() {
        return this.obstacleY;
    }

    public boolean obstacleHasCollided() {
        return this.obstacleHasCollided;
    }

    public int getObstacleWidth() {
        return this.obstacleWidth;
    }

    public int getObstacleHeight() {
        return this.obstacleHeight;
    }

    public void hasCollided(boolean hasCollided) {
        if (hasCollided) {
            this.obstacleHasCollided = hasCollided;
        }
    }

    public void resetObstacleCoordinates(int newXValue, int newYValue){
        this.obstacleX=newXValue;
        this.obstacleY=newYValue;
        this.obstacleHasCollided=false;
    }
}

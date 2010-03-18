
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class Pelota{

    private int x;
    private int y;
    private Image bullet;
    private int vehicleOrEnemy;
    private boolean hasCollided=false;

    public Pelota(int x, int y, int vehicleOrEnemy) {
        this.vehicleOrEnemy = vehicleOrEnemy;
        this.x = x;
        this.y = y;
        try {
            bullet = Image.createImage("/bullet.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void actualizar() {
        if(vehicleOrEnemy==1)
            x += 15;
        else
            x-=9;
    }

    public void dibujar(Graphics g) {
        g.drawImage(bullet,x, y, g.TOP | g.LEFT);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return this.y;
    }

    public void hasCollided(boolean hasCollided){
        if(hasCollided){
            System.out.println("Bullet hit something!");
            this.hasCollided = hasCollided;
        }
    }

    public boolean returnHasCollided(){
        return this.hasCollided;
    }
}
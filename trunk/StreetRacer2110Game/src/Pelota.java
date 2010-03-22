
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Pelota{
    private static final int VEHICLE_BULLET_X_CHANGE = 15;
    private static final int ENEMY_BULLET_X_CHANGE = 9;

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
            x += VEHICLE_BULLET_X_CHANGE;
        else
            x-=ENEMY_BULLET_X_CHANGE;
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
            this.hasCollided = hasCollided;
        }
    }

    public boolean returnHasCollided(){
        return this.hasCollided;
    }
}
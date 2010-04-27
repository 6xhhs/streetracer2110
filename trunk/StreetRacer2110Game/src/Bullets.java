




import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Representa una pelota que estara moviendose en el campo de juego
 * hay que documentar todo lo que es publico, como esta clase, metodos y variables(constante)
 * los objetos privados se documentan normalmente, haciendole comentarios al lado de su
 * declaracion
 * @author Manuel Gonzalez Solano, Salvador Aguilar Galindo
 * @version 1.0, Abril, 2010
 */
public class Bullets {

    private static final int VEHICLE_BULLET_X_CHANGE = 15;
    private static final int ENEMY_BULLET_X_CHANGE = 9;
    private int x;
    private int y;
    private Image bullet;
    private int vehicleOrEnemy;
    private boolean hasCollided = false;
    private boolean canFireBullet;
    //private boolean canActualize;

    /**
     * Constructor, crea una pelota con una imagen desde un archivo, y dimensiones dadas
     * por los parametros
     * @param x ancho de la pelota
     * @param y alto de la pelota
     * @param vehicleOrEnemy indicador de tipo de personaje quien usara la pelota
     */
    public Bullets(int x, int y, int vehicleOrEnemy) {
        this.vehicleOrEnemy = vehicleOrEnemy;
        this.x = x;
        this.y = y;
        this.canFireBullet = true;
        try {
            bullet = Image.createImage("/bullet.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void actualizar() {
            if (vehicleOrEnemy == 1) {
                x += VEHICLE_BULLET_X_CHANGE;
            } else {
                x -= ENEMY_BULLET_X_CHANGE;
            }
    }

    public void dibujar(Graphics g) {
            g.drawImage(bullet, x, y, g.TOP | g.LEFT);
    }

    public int getX() {
        return x;
    }

    /**
     * Accesor de la altura de la pelota
     * @return
     */
    public int getY() {
        return this.y;
    }

    /**
     * 
     * @param hasCollided
     */
    public void hasCollided(boolean hasCollided) {
        if (hasCollided) {
            this.hasCollided = hasCollided;
        }
    }

    /**
     *
     * @return
     */
    public boolean returnHasCollided() {
        return this.hasCollided;
    }

    public boolean getCanFireBullet() {
        return this.canFireBullet;
    }

    public void setCanFireBullet(boolean canFireBullet) {
        this.canFireBullet = canFireBullet;
    }

    public void setCoords(int bullX, int bullY) {
        this.x = bullX;
        this.y = bullY;
    }

//    public void setCanActualize(boolean canActualize) {
//        this.canActualize = canActualize;
//    }
//
//    public boolean getCanActualize() {
//        return this.canActualize;
//    }

    public void resetCoords(){
        this.x=0;
        this.y=0;
    }
}

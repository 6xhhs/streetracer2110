
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
// Comentario de RMR
public class Enemies {

    private Image enemiesImage;
    private Image enemiesCollidedImage;
    private int x;
    private int y;
    private int bulletX;
    private int bulletY;
    private Vector bullets;
    private final int ENEMY_WIDTH;
    private final int ENEMY_HEIGHT;
    private int enemiesSelectedIndex;
    private static Random random = new Random();
    private int changeInX;
    private int bulletChangeInX;
    private boolean enemyHasCollided = false;
    private int addBulletCount=0;

    public Enemies(int xCoordinate, int yCoordinate, int enemiesSelectedIndex) {

        this.enemiesSelectedIndex = enemiesSelectedIndex;
        this.x = xCoordinate;
        this.y = yCoordinate;
        bulletChangeInX = 9;
        changeInX = random.nextInt(5) + 3;
        if (this.enemiesSelectedIndex == 0) {
            try {
                enemiesImage = Image.createImage("/Motocicleta.png");
                enemiesCollidedImage = Image.createImage("/explosion.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ENEMY_WIDTH = 83;
            ENEMY_HEIGHT = 42;
        } else if (this.enemiesSelectedIndex == 1) {
            try {
                enemiesImage = Image.createImage("/motorcycle.png");
                enemiesCollidedImage = Image.createImage("/explosion.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ENEMY_WIDTH = 85;
            ENEMY_HEIGHT = 45;
        } else {
            try {
                enemiesImage = Image.createImage("/Motocicleta.png");
                enemiesCollidedImage = Image.createImage("/explosion.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ENEMY_WIDTH = 83;
            ENEMY_HEIGHT = 42;
        }

        bullets = new Vector();
    }

    public void dibujar(Graphics g) {

        g.drawImage(enemiesImage, x, y, g.TOP | g.LEFT);
    }

    public void actualizar() {
        this.x -= changeInX;

    }

    public void agregarBullet() {
        addBulletCount++;
        if (addBulletCount>=40) {
            bullets.addElement(new Pelota(this.bulletX, this.bulletY, 2));
            addBulletCount=0;
        }

    }

    public void dibujarFireGun(Graphics g) {
        for (int i = this.bullets.size() - 1; i >= 0; i--) {
            ((Pelota) bullets.elementAt(i)).dibujar(g);
        }
    }

    public void actualizarFireGun() {
        for (int i = this.bullets.size() - 1; i >= 0; i--) {
            if (((Pelota) bullets.elementAt(i)).getX() < 0 || ((Pelota) bullets.elementAt(i)).returnHasCollided()) {
                bullets.removeElementAt(i);
            } else {
                ((Pelota) bullets.elementAt(i)).actualizar();
            }
        }
    }

    public void setBulletX() {
        if (enemiesSelectedIndex == 0) {
            this.bulletX = this.x - 5;
        } else if (enemiesSelectedIndex == 1) {
            this.bulletX = this.x - 5;
        } else {
            this.bulletX = this.x - 5;
        }
    }

    public void setBulletY() {
        if (enemiesSelectedIndex == 0) {
            this.bulletY = this.y;
        } else if (enemiesSelectedIndex == 1) {
            this.bulletY = this.y;
        } else {
            this.bulletY = this.y;
        }
    }

    public int getEnemyX() {
        return this.x;
    }

    public int getEnemyY() {
        return this.y;
        //+ (ENEMY_HEIGHT/2);
    }

    public int getEnemyWidth() {
        return this.ENEMY_WIDTH;
    }

    public int getEnemyHeight() {
        return this.ENEMY_HEIGHT;
    }

    public void hasCollided(boolean hasCollided) {
        if (hasCollided) {
            System.out.println("Enemy has collided: BOOM!");
            enemiesImage = enemiesCollidedImage;
            this.enemyHasCollided = hasCollided;
        }
    }

    public boolean returnEnemyHasCollided() {
        return this.enemyHasCollided;
    }

    public void checkEnemyBulletsVehicleCollision(Vehicle vehicle) {
        if (bullets != null && vehicle != null) {
            for (int i = bullets.size() - 1; i >= 0; i--) {

                if (((Pelota) bullets.elementAt(i)).getX() < vehicle.getVehicleX() + vehicle.getVehicleWidth()
                        && ((Pelota) bullets.elementAt(i)).getX() > vehicle.getVehicleX()
                        && ((Pelota) bullets.elementAt(i)).getY() > vehicle.getVehicleY()
                        && ((Pelota) bullets.elementAt(i)).getY() < vehicle.getVehicleHeight() + vehicle.getVehicleY()
                        && ((Pelota) bullets.elementAt(i)).getX() > 0) {

                    ((Pelota) bullets.elementAt(i)).hasCollided(true);
                    vehicle.hasCollided(true);
                }
            }
        }
    }
}

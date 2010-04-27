




import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Se encarga de manejar los enemigos que apareceran en el juego
 * @author Salvador Aguilar Galindo, Manuel González Solano
 * @version 1.0, Abril 2010
 */
public class Enemies {

    public static final int MIN_SPEED = 4;
    private static final int CREATE_BULLET_DELAY_TIME = 40;
    private Image originalEnemiesImage;
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
    private boolean enemyHasCollided = false;
    private int addBulletCount = 0;
    private static final int BULLET_TYPE_INDEX = 2;
    private int bulletsVectorSize;
/**
 * Constructor, crea un nuevo enemigo segun el índice de selección de enemigo,
 * creándolo en la posición dada.
 * @param xCoordinate posición en x que tomará el enemigo
 * @param yCoordinate posición en y que tomará el enemigo
 * @param enemiesSelectedIndex índice de de selección de enemigo
 */
    public Enemies(int xCoordinate, int yCoordinate, int enemiesSelectedIndex) {

        bulletsVectorSize = 0;

        this.enemiesSelectedIndex = enemiesSelectedIndex;
        this.x = xCoordinate;
        this.y = yCoordinate;
        changeInX = random.nextInt(5) + MIN_SPEED;
        if (this.enemiesSelectedIndex == 0) {
            try {
                originalEnemiesImage = Image.createImage("/motorcycle.png");
                enemiesImage = Image.createImage("/motorcycle.png");
                enemiesCollidedImage = Image.createImage("/explosion.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ENEMY_WIDTH = 75;
            ENEMY_HEIGHT = 40;
        } else if (this.enemiesSelectedIndex == 1) {
            try {
                originalEnemiesImage = Image.createImage("/Tk.png");
                enemiesImage = Image.createImage("/Tk.png");
                enemiesCollidedImage = Image.createImage("/explosion.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ENEMY_WIDTH = 90;
            ENEMY_HEIGHT = 47;
        } else {
            try {
                originalEnemiesImage = Image.createImage("/motorcycle.png");
                enemiesImage = Image.createImage("/motorcycle.png");
                enemiesCollidedImage = Image.createImage("/explosion.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ENEMY_WIDTH = 75;
            ENEMY_HEIGHT = 40;
        }

        bullets = new Vector();
    }

    /**
     * Dibuja la imagen del enemigo dado en la pantalla
     * @param g es el canvas del juego actual
     */
    public void draw(Graphics g) {
        g.drawImage(enemiesImage, x, y, g.TOP | g.LEFT);
    }

    /**
     * actualiza la posicion x del enemigo
     */
    public void update() {
        this.x -= changeInX;
    }

    /**
     * decide si el enemigo actual puede disparar una bala
     */
    public void addBullet() {
        addBulletCount++;
        if (addBulletCount >= CREATE_BULLET_DELAY_TIME) {
            setBulletX();
            setBulletY();
            bullets.addElement(new Bullets(this.bulletX, this.bulletY, BULLET_TYPE_INDEX));
            addBulletCount = 0;
        }

    }

    /**
     * dibuja la bala desde la pistola del enemigo actual
     * @param g es el canvas del juego actual
     */
    public void drawAmmo(Graphics g) {
        //optimized here
        bulletsVectorSize = this.bullets.size() - 1;
        for (int i = bulletsVectorSize; i >= 0; i--) {
            ((Bullets) bullets.elementAt(i)).dibujar(g);
        }
    }

    /**
     * decide si actualiza o destruye las balas del enemigo
     * si es que la bala se haya salido de la pantalla, haya
     * chocado o sigua viva
     */
    public void updateAmmo() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            if (((Bullets) bullets.elementAt(i)).getX() < 0 || ((Bullets) bullets.elementAt(i)).returnHasCollided()) {
                bullets.removeElementAt(i);
            } else {
                ((Bullets) bullets.elementAt(i)).actualizar();
            }
        }
    }

    /**
     * decide en dónde queda la posición x de la pistola del enemigo, asignándole tal posición
     * al valor x de una bala
     */
    public void setBulletX() {
        if (enemiesSelectedIndex == 0) {
            this.bulletX = this.x - 5;
        } else if (enemiesSelectedIndex == 1) {
            this.bulletX = this.x + ENEMY_WIDTH / 2;
        } else {
            this.bulletX = this.x - 5;
        }
    }

    /**
     * decide en dónde queda la posición y de la pistola del enemigo, asignándole tal posición
     * al valor y de una bala
     */
    public void setBulletY() {
        if (enemiesSelectedIndex == 0) {
            this.bulletY = this.y;
        } else if (enemiesSelectedIndex == 1) {
            this.bulletY = this.y + ENEMY_HEIGHT / 3;
        } else {
            this.bulletY = this.y;
        }
    }

    /**
     *
     * @return la posición x del enemigo
     */
    public int getEnemyX() {
        return this.x;
    }

    /**
     *
     * @return la posición y del enemigo
     */
    public int getEnemyY() {
        return this.y;
    }

    /**
     *
     * @return qué tan ancho es el enemigo
     */
    public int getEnemyWidth() {
        return this.ENEMY_WIDTH;
    }

    /**
     *
     * @return qué tan alto es el enemigo
     */
    public int getEnemyHeight() {
        return this.ENEMY_HEIGHT;
    }

    /**
     * identifica una colisión por parte del enemigo
     * @param hasCollided el valor que indica si hay una colisión o no
     */
    public void hasCollided(boolean hasCollided) {
        if (hasCollided) {
            enemiesImage = enemiesCollidedImage;
            this.enemyHasCollided = hasCollided;
        }
    }

    /**
     *
     * @return el valor que indica si hay una colisión por parte del enemigo
     */
    public boolean returnEnemyHasCollided() {
        return this.enemyHasCollided;
    }

    /**
     * revisa el Vector de balas para identificar colisiones por parte de las misma
     * con un vehículo dado
     * @param vehicle el vehículo actual del juego
     */
    public void checkBulletsVehicleCollisions(Vehicle vehicle) {
        //optimized here
        if (bullets != null && vehicle != null) {
            for (int i = bullets.size() - 1; i >= 0; i--) {

                if (((Bullets) bullets.elementAt(i)).getX() < vehicle.getVehicleX() + vehicle.getVehicleWidth()
                        && ((Bullets) bullets.elementAt(i)).getX() > vehicle.getVehicleX()
                        && ((Bullets) bullets.elementAt(i)).getY() > vehicle.getVehicleY()
                        && ((Bullets) bullets.elementAt(i)).getY() < vehicle.getVehicleHeight() + vehicle.getVehicleY()
                        && ((Bullets) bullets.elementAt(i)).getX() > 0) {

                    ((Bullets) bullets.elementAt(i)).hasCollided(true);
                    vehicle.hasCollided(true, false);
                }
            }
        }
    }

    /**
     * reinicia la posición del enemigo
     * @param newXValue la nueva posición en x del enemigo
     * @param newYValue la nueva posición en y del enemigo
     */
    public void resetEnemCoords(int newXValue, int newYValue) {
        this.x = newXValue;
        this.y = newYValue;
    }

    /**
     * reinicia a la variable encargada de identificar si hay una colisión o no.
     */
    public void resetHasCollided() {
        this.enemyHasCollided = false;
    }

    /**
     * reinicia la imagen del enemigo
     */
    public void resetEnemImage() {
        this.enemiesImage = originalEnemiesImage;
    }

    /**
     * reincia a todos los valores del enemigo
     * @param newXValue la nueva posición en x del enemigo
     * @param newYValue la nueva posición en y del enemigo
     */
    public void resetEnemy(int newXValue, int newYValue) {
        resetEnemCoords(newXValue, newYValue);
        resetHasCollided();
        resetEnemImage();
    }

    /**
     * elimina todas las balas que están dentro del vector de balas del enemigo
     */
    public void removeAllBullets() {
        this.bullets.removeAllElements();
    }
}






import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
 /*
 * Se encarga de manejar al auto que aparecerá en la pantalla
  * durante el juego.
 * @author Salvador Aguilar Galindo, Manuel González Solano
 * @version 1.0, Abril 2010
 */
public class Vehicle {

    //private static final int MAX_BULLETS = 3;
    private static final int ROAD_TOP_Y_LIMIT = 260;
    private int x;
    private int y;
    private int screenWidth;
    private int screenHeight;
    private int bulletX;
    private int bulletY;
    private Vector bullets;
    private final int CAR_WIDTH;
    private final int CAR_HEIGHT;
    private int carSelectedIndex;
    private int changeInX;
    private int changeInY;
    private Image lifeImage;
    private int damageCount;
    private int totalDamageCount;
    private int totalDamageReceived;
    private int totalPointsAccumulated;
    private Font font;
    private boolean gameOverIsActive;
    private Vector lifeBarImages;
    //private static final int BULLET_TYPE_INDEX = 1;
    private boolean vehicleIsAtRamp;
    private int carJumpingRampCount;
    private boolean vehicleIsRising;
    private boolean drawNormalVehicleIsActive;
    private boolean drawDamagedVehicleIsActive;
    private boolean drawRisingVehicleIsActive;
    private Vector vehicleImages;
    private int bulletsVectorSize;
    /**
     * Constructor, indica que auto se seleccionó, después, le asigna sus valores,
     * dependiendo del tamaño, así como la velocidad que tendrán
     * y el escudo que tendrán contra las balas, agrega una barra de vida
     * acorde al auto elejido
     * @param screenWidth determina el ancho de la pantalla
     * @param screenHeight determina el alto de la pantalla
     * @param carSelectedIndex indica el auto seleccionado
     */
    public Vehicle(int screenWidth, int screenHeight, int carSelectedIndex) {

        this.vehicleImages = new Vector();

        this.vehicleIsRising = true;
        this.carJumpingRampCount = 0;
        this.vehicleIsAtRamp = false;
        this.carSelectedIndex = carSelectedIndex;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.x = 0;
        this.y = this.screenHeight - (screenHeight / 4);

        if (this.carSelectedIndex == 0) {
            try {
                vehicleImages.addElement(Image.createImage("/S Racer.png"));
                vehicleImages.addElement(Image.createImage("/S Racer Damaged.png"));
                vehicleImages.addElement(Image.createImage("/S Racer Ramp.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            CAR_WIDTH = 100;
            CAR_HEIGHT = 49;
            changeInX = 10;
            changeInY = 10;
            totalDamageReceived = 1;
        } else if (this.carSelectedIndex == 1) {
            try {

                vehicleImages.addElement(Image.createImage("/M Racer.png"));
                vehicleImages.addElement(Image.createImage("/M Racer Damaged.png"));
                vehicleImages.addElement(Image.createImage("/M Racer Ramp.png"));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            CAR_WIDTH = 114;
            CAR_HEIGHT = 65;
            changeInX = 5;
            changeInY = 5;
            totalDamageReceived = 3;
        } else {
            try {

                vehicleImages.addElement(Image.createImage("/SM Racer.png"));
                vehicleImages.addElement(Image.createImage("/SM Racer Damaged.png"));
                vehicleImages.addElement(Image.createImage("/SM Racer Ramp.png"));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            CAR_WIDTH = 98;
            CAR_HEIGHT = 65;
            changeInX = 7;
            changeInY = 7;
            totalDamageReceived = 2;
        }

        lifeBarImages = new Vector();

        try {
            lifeBarImages.addElement(Image.createImage("/life bar 1.png"));
            lifeBarImages.addElement(Image.createImage("/life bar 2.png"));
            lifeBarImages.addElement(Image.createImage("/life bar 3.png"));
            lifeBarImages.addElement(Image.createImage("/life bar 4.png"));
            lifeBarImages.addElement(Image.createImage("/life bar background.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        lifeImage = (Image) lifeBarImages.elementAt(0);

        this.drawNormalVehicleIsActive = true;
        this.drawRisingVehicleIsActive = false;
        drawDamagedVehicleIsActive = false;

        gameOverIsActive = false;
        totalPointsAccumulated = 0;
        damageCount = 0;
        totalDamageCount = 0;
        bullets = new Vector();
        for (int i = 0; i < 3; i++) {
            bullets.addElement(new Bullets(0, 0, 1));
        }
        bulletsVectorSize = 3;

        font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
    }
    /**
     * Dibujara al auto dependiendo del estado en el que se presente,
     * su vida y puntos que lleva el jugador.
     * @param g graphics de la pantalla actual
     */
    public void draw(Graphics g) {
        g.drawImage((Image) lifeBarImages.elementAt(4), 0, 0, g.TOP | g.LEFT);

        if (drawNormalVehicleIsActive) {
            g.drawImage((Image) vehicleImages.elementAt(0), x, y, g.TOP | g.LEFT);
        } else if (drawDamagedVehicleIsActive) {
            g.drawImage((Image) vehicleImages.elementAt(1), x, y, g.TOP | g.LEFT);
            drawDamagedVehicleIsActive = false;
            drawNormalVehicleIsActive = true;
        } else if (drawRisingVehicleIsActive) {
            g.drawImage((Image) vehicleImages.elementAt(2), x, y, g.TOP | g.LEFT);
        }
        if (!gameOverIsActive) {
            g.drawImage(lifeImage, 0, 0, g.TOP | g.LEFT);
        }
        g.setFont(font);
        g.setColor(0xff6600);

        g.drawString("Score: " + totalPointsAccumulated, 191, 8, g.TOP | g.LEFT);
    }

    /**
     * controla el movimiento hacia la izquierda del auto, tomando en cuenta
     * los límites de la pantalla.
     */
    public void moveLeft() {
        if (x <= 0) {
            x = 0;
        } else {
            x -= changeInX;
        }
    }

    /**
     * controla el movimiento hacia la derecha del auto, tomando en cuenta
     * los límites de la pantalla.
     */
    public void moveRight() {
        if (x >= screenWidth - CAR_WIDTH) {
            x = screenWidth - CAR_WIDTH;
        } else {
            x += changeInX;
        }
    }
    /**
     * controla el movimiento hacia arriba del auto, tomando en cuenta
     * los límites de la carretera.
     */
    public void moveUp() {
        if (y <= ROAD_TOP_Y_LIMIT - CAR_HEIGHT) {
            y = ROAD_TOP_Y_LIMIT - CAR_HEIGHT;
        } else {
            y -= changeInY;
        }
    }
/**
 * controla el movimiento hacia abajo del auto, tomando en cuenta
     * los límites de la pantalla.
 */
    public void moveDown() {
        if (y >= screenHeight - CAR_HEIGHT) {
            y = screenHeight - CAR_HEIGHT;
        } else {
            y += changeInY;
        }
    }
    /**
     * actualiza el estado del vehículo, dependiendo de su posición en el juego
     * con respecto a la rampa final.
     * @param carJumpingRampCount indica si el auto está subiendo o bajando la rampa
     */
    public void update(int carJumpingRampCount) {
        if (vehicleIsRising) {
            drawDamagedVehicleIsActive = false;
            drawNormalVehicleIsActive = false;
            drawRisingVehicleIsActive = true;
            makeVehicleRise(carJumpingRampCount);
        } else {
            drawRisingVehicleIsActive = false;
            drawNormalVehicleIsActive = true;
            makeVehicleFall();
        }
    }

    /**
     * activa una bala si es que existe una que no esté activa actualmente.
     */
    public void addBullet() {
        for (int i = 0; i < bulletsVectorSize; i++) {
            if (((Bullets) bullets.elementAt(i)).getCanFireBullet()) {
                setBulletCoords(i);
                ((Bullets) bullets.elementAt(i)).setCanFireBullet(false);
                break;
            }
        }
    }

    /**
     * dibuja las balas del auto en la pantalla.
     * @param g graphics de la pantalla actual
     */
    public void drawAmmo(Graphics g) {
        for (int i = 0; i < bulletsVectorSize; i++) {
            if (!((Bullets) bullets.elementAt(i)).getCanFireBullet()) {
                ((Bullets) bullets.elementAt(i)).dibujar(g);
            }
        }
    }

    /**
     * actualiza el estado de las balas, activando las que se hayan salido
     * de fuera de pantalla o las que hayan colisionado, y actualizando las que
     * se encuentran 'vivas'.
     */
    public void updateAmmo() {
        for (int i = 0; i < bulletsVectorSize; i++) {
            if (!((Bullets) bullets.elementAt(i)).getCanFireBullet()) {
                if (((Bullets) bullets.elementAt(i)).getX() >= (screenWidth + 5)) {
                    ((Bullets) bullets.elementAt(i)).resetCoords();
                    ((Bullets) bullets.elementAt(i)).setCanFireBullet(true);
                } else {
                    ((Bullets) bullets.elementAt(i)).actualizar();
                }
            }
        }
    }

    /**
     * establece la posición x de una bala a ser creada de acuerdo a la
     * ubicación de la pistola del auto actual.
     */
    public void setBulletX() {
        if (carSelectedIndex == 0) {
            this.bulletX = this.x + 85;
        } else if (carSelectedIndex == 1) {
            this.bulletX = this.x + 79;
        } else {
            this.bulletX = this.x + 70;
        }
    }

    /**
     * establece la posición y de una bala a ser creada de acuerdo a la
     * ubicación de la pistola del auto actual.
     */
    public void setBulletY() {
        if (carSelectedIndex == 0) {
            this.bulletY = this.y;
        } else {
            this.bulletY = this.y + 3;
        }
    }

    /**
     * cambia el estado del auto con respecto a las colisiones, agregando
     * el puntaje adecuado si es que haya colisionado con un enemigo.
     * @param hasCollided indica si hay una colisión o no
     * @param addPoints indica si debe agregar puntos por haber colisionado
     * con un enemigo o no
     */
    public void hasCollided(boolean hasCollided, boolean addPoints) {
        if (hasCollided) {
            drawNormalVehicleIsActive = false;
            drawDamagedVehicleIsActive = true;
            addPoints(addPoints);
            updateDamage();
        }
    }

    /**
     * reinicia las banderas booleanas del auto.
     */
    private void resetBoolFlags() {
        this.vehicleIsAtRamp = false;
        this.vehicleIsRising = true;
        drawNormalVehicleIsActive = true;
        drawDamagedVehicleIsActive = false;
        drawRisingVehicleIsActive = false;
        gameOverIsActive = false;
    }

    /**
     * reinicia las balas del vehículo, tanto en posición como
     * en el estado que se encuentran.
     */
    private void resetBullets() {
        for (int i = 0; i < bulletsVectorSize; i++) {
            ((Bullets) bullets.elementAt(i)).resetCoords();
            ((Bullets) bullets.elementAt(i)).setCanFireBullet(true);
        }
    }

    /**
     * reinicia la posición del vehículo.
     */
    private void resetCoords() {
        this.x = 0;
        this.y = this.screenHeight - (screenHeight / 4);
    }

    /**
     * reinicia a los indicadores de la salud del vehículo.
     */
    private void resetDamage() {
        damageCount = 0;
        totalDamageCount = 0;
    }

    /**
     * actualiza al estado de salud del vehículo, activando a la bandera de
     * juego perdido se se ha agotado.
     */
    private void updateDamage() {
        damageCount++;
        if (damageCount == totalDamageReceived) {
            damageCount = 0;
            totalDamageCount++;
            if (totalDamageCount == 1) {
                lifeImage = (Image) lifeBarImages.elementAt(1);
            } else if (totalDamageCount == 2) {
                lifeImage = (Image) lifeBarImages.elementAt(2);
            } else if (totalDamageCount == 3) {
                lifeImage = (Image) lifeBarImages.elementAt(3);
            } else if (totalDamageCount == 4) {
                this.gameOverIsActive = true;
            }
        }
    }

    /**
     * agrega puntos al puntaje total.
     * @param addPoints indica si se debe agregar puntos o no
     */
    private void addPoints(boolean addPoints) {
        if (addPoints) {
            totalPointsAccumulated += 15;
        }
    }

    /**
     *
     * @return la posición en x del vehículo.
     */
    public int getVehicleX() {
        return this.x;
    }
/**
 *
 * @return el ancho del vehículo.
 */
    public int getVehicleWidth() {
        return this.CAR_WIDTH;
    }

    /**
     *
     * @return la posición en y del vehículo.
     */
    public int getVehicleY() {
        return this.y;
    }

    /**
     *
     * @return la altura del vehículo.
     */
    public int getVehicleHeight() {
        return this.CAR_HEIGHT;
    }

    /**
     * determina si hubo una colisión entre una bala del auto actual y algun enemigo dentro
     * del vector de enemigos dado, agregando puntos y eliminando a los objetos involucrados
     * si esto se cumple.
     * @param enemies conjunto de enemigos dentro del juego
     */
    public void checkBulletsEnemCollision(Vector enemies) {
        int enemiesVectorSize = enemies.size() - 1;
        if (bullets != null && enemies != null) {
            for (int i = 0; i < bulletsVectorSize; i++) {

                for (int j = enemiesVectorSize; j >= 0; j--) {
                    if (((Bullets) bullets.elementAt(i)).getX() > ((Enemies) enemies.elementAt(j)).getEnemyX()
                            && ((Bullets) bullets.elementAt(i)).getX() < ((Enemies) enemies.elementAt(j)).getEnemyX() + ((Enemies) enemies.elementAt(j)).getEnemyWidth()
                            && ((Bullets) bullets.elementAt(i)).getY() > ((Enemies) enemies.elementAt(j)).getEnemyY()
                            && ((Bullets) bullets.elementAt(i)).getY() < ((Enemies) enemies.elementAt(j)).getEnemyY() + ((Enemies) enemies.elementAt(j)).getEnemyHeight()
                            && ((Bullets) bullets.elementAt(i)).getX() < screenWidth) {

                        ((Bullets) bullets.elementAt(i)).resetCoords();
                        ((Bullets) bullets.elementAt(i)).setCanFireBullet(true);
                        ((Enemies) enemies.elementAt(j)).hasCollided(true);
                        totalPointsAccumulated += 30;
                    }

                }
            }
        }
    }

    /**
     *
     * @return regresa el estado de juego perdido.
     */
    public boolean getGameOver() {
        return this.gameOverIsActive;
    }

    /**
     * determina el estado de juego perdido.
     * @param indica si el jugador a perdido o no
     */
    public void setGameOver(boolean flag) {
        this.gameOverIsActive = false;
    }

    /**
     * reinicia los valores del vehículo actual, tanto su posición y estado de salud
     * como sus balas y puntaje.
     */
    public void resetValues() {
        this.carJumpingRampCount = 0;
        totalPointsAccumulated = 0;
        resetCoords();
        resetBoolFlags();
        resetDamage();
        resetBullets();
        lifeImage = (Image) lifeBarImages.elementAt(0);
    }

    /**
     *
     * @return total de puntos acumulados.
     */
    public int getTotalPoints() {
        return this.totalPointsAccumulated;
    }

    /**
     * determina si el vehículo ha llegado a la rampa.
     * @param vehicleIsAtRamp indica si el vehículo está en la rampa o no
     */
    public void hasCollidedWithRamp(boolean vehicleIsAtRamp) {
        this.vehicleIsAtRamp = vehicleIsAtRamp;
    }

    /**
     * hace que la imagen del vehículo gire para desplazarse a lo largo
     * de la rampa.
     * @param carJumpingRampCount indica el tiempo que se demora el vehículo
     * en librar la rampa
     */
    private void makeVehicleRise(int carJumpingRampCount) {
        if (this.carJumpingRampCount <= carJumpingRampCount) {
            this.y -= 7;
            this.carJumpingRampCount++;
        } else {
            this.vehicleIsRising = false;
        }
    }

    /**
     * cuando el vehículo haya librado la rampa, gira su imagen para empezar
     * el aterrizar.
     */
    private void makeVehicleFall() {
        if (this.carJumpingRampCount >= 0) {
            this.y += 7;
            this.carJumpingRampCount--;
        } else {
            vehicleIsAtRamp = false;
        }
    }

    /**
     *
     * @return la posición del vehículo con respecto a la rampa.
     */
    public boolean getIsAtRamp() {
        return this.vehicleIsAtRamp;
    }

    /**
     * establece la posición de una bala del vehículo.
     * @param i indica qué bala dentro del vector de balas será activada.
     */
    private void setBulletCoords(int i) {
        setBulletX();
        setBulletY();
        ((Bullets) bullets.elementAt(i)).setCoords(this.bulletX, this.bulletY);
    }

    public void increaseHealth(){
        this.damageCount = 0;
        if(this.totalDamageCount > 0){
            this.totalDamageCount--;
            if(totalDamageCount==0){
                lifeImage = (Image) lifeBarImages.elementAt(0);
            }else if (totalDamageCount == 1) {
                lifeImage = (Image) lifeBarImages.elementAt(1);
            } else if (totalDamageCount == 2) {
                lifeImage = (Image) lifeBarImages.elementAt(2);
            } else if (totalDamageCount == 3) {
                lifeImage = (Image) lifeBarImages.elementAt(3);
            }
        }
    }
}

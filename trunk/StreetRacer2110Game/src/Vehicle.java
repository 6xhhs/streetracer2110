
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Vehicle {

    private static final int MAX_BULLETS = 4;
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
    private static final int BULLET_TYPE_INDEX = 1;
    private boolean vehicleIsAtRamp;
    private int carJumpingRampCount;
    private boolean vehicleIsRising;
    private boolean drawNormalVehicleIsActive;
    private boolean drawDamagedVehicleIsActive;
    private boolean drawRisingVehicleIsActive;
    private Vector vehicleImages;

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
                vehicleImages.addElement(Image.createImage("/S Racer Ramp.png"));

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
                vehicleImages.addElement(Image.createImage("/S Racer Ramp.png"));

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

        font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
    }

    public void dibujar(Graphics g) {
        g.drawImage((Image) lifeBarImages.elementAt(4), 0, 0, g.TOP | g.LEFT);

        if(drawNormalVehicleIsActive){
            g.drawImage((Image)vehicleImages.elementAt(0), x, y, g.TOP|g.LEFT);
        }else if (drawDamagedVehicleIsActive) {
            g.drawImage((Image)vehicleImages.elementAt(1), x, y, g.TOP|g.LEFT);
            drawDamagedVehicleIsActive = false;
            drawNormalVehicleIsActive = true;
        } else if(drawRisingVehicleIsActive) {
            g.drawImage((Image)vehicleImages.elementAt(2), x, y, g.TOP|g.LEFT);
        }
        if (!gameOverIsActive) {
            g.drawImage(lifeImage, 0, 0, g.TOP | g.LEFT);
        }
        g.setFont(font);
        g.setColor(0xff6600);

        g.drawString("Score: " + totalPointsAccumulated, 190, 8, g.TOP | g.LEFT);
    }

    public void moveLeft() {
        if (x <= 0) {
            x = 0;
        } else {
            x -= changeInX;
        }
    }

    public void moveRight() {
        if (x >= screenWidth - CAR_WIDTH) {
            x = screenWidth - CAR_WIDTH;
        } else {
            x += changeInX;
        }
    }

    public void moveUp() {
        if (y <= ROAD_TOP_Y_LIMIT - CAR_HEIGHT) {
            y = ROAD_TOP_Y_LIMIT - CAR_HEIGHT;
        } else {
            y -= changeInY;
        }
    }

    public void moveDown() {
        if (y >= screenHeight - CAR_HEIGHT) {
            y = screenHeight - CAR_HEIGHT;
        } else {
            y += changeInY;
        }
    }

    public void actualizar(int carJumpingRampCount) {
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

    public void agregarBullet() {
        if (bullets.size() < MAX_BULLETS) {
            bullets.addElement(new Pelota(this.bulletX, this.bulletY, BULLET_TYPE_INDEX));
        }
    }

    public void dibujarFireGun(Graphics g) {
        for (int i = this.bullets.size() - 1; i >= 0; i--) {
            ((Pelota) bullets.elementAt(i)).dibujar(g);
        }
    }

    public void actualizarFireGun() {
        for (int i = this.bullets.size() - 1; i >= 0; i--) {
            if (((Pelota) bullets.elementAt(i)).getX() >= (screenWidth + 5) || ((Pelota) bullets.elementAt(i)).returnHasCollided()) {
                bullets.removeElementAt(i);
            } else {
                ((Pelota) bullets.elementAt(i)).actualizar();
            }
        }
    }

    public void setBulletX() {
        if (carSelectedIndex == 0) {
            this.bulletX = this.x + 85;
        } else if (carSelectedIndex == 1) {
            this.bulletX = this.x + 79;
        } else {
            this.bulletX = this.x + 70;
        }
    }

    public void setBulletY() {
        if (carSelectedIndex == 0) {
            this.bulletY = this.y;
        } else {
            this.bulletY = this.y + 3;
        }
    }

    public void hasCollided(boolean hasCollided, boolean addPoints) {
        if (hasCollided) {
            drawNormalVehicleIsActive = false;
            drawDamagedVehicleIsActive = true;

            if (addPoints) {
                totalPointsAccumulated += 15;
            }
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
    }

    public int getVehicleX() {
        return this.x;
    }

    public int getVehicleWidth() {
        return this.CAR_WIDTH;
    }

    public int getVehicleY() {
        return this.y;
    }

    public int getVehicleHeight() {
        return this.CAR_HEIGHT;
    }

    public void checkBulletsEnemyCollision(Vector enemies) {
        if (bullets != null && enemies != null) {
            for (int i = bullets.size() - 1; i >= 0; i--) {

                for (int j = enemies.size() - 1; j >= 0; j--) {
                    if (((Pelota) bullets.elementAt(i)).getX() > ((Enemies) enemies.elementAt(j)).getEnemyX()
                            && ((Pelota) bullets.elementAt(i)).getX() < ((Enemies) enemies.elementAt(j)).getEnemyX() + ((Enemies) enemies.elementAt(j)).getEnemyWidth()
                            && ((Pelota) bullets.elementAt(i)).getY() > ((Enemies) enemies.elementAt(j)).getEnemyY()
                            && ((Pelota) bullets.elementAt(i)).getY() < ((Enemies) enemies.elementAt(j)).getEnemyY() + ((Enemies) enemies.elementAt(j)).getEnemyHeight()
                            && ((Pelota) bullets.elementAt(i)).getX() < screenWidth) {

                        ((Pelota) bullets.elementAt(i)).hasCollided(true);
                        ((Enemies) enemies.elementAt(j)).hasCollided(true);
                        totalPointsAccumulated += 30;
                    }

                }
            }
        }
    }

    public boolean returnGameOver() {
        return this.gameOverIsActive;
    }

    public void setGameOver(boolean flag) {
        this.gameOverIsActive = false;
    }

    void resetValues() {
        this.carJumpingRampCount = 0;
        this.vehicleIsAtRamp = false;
        this.vehicleIsRising = true;
        this.x = 0;
        this.y = this.screenHeight - (screenHeight / 4);
        drawNormalVehicleIsActive = true;
        drawDamagedVehicleIsActive = false;
        drawRisingVehicleIsActive = false;
        gameOverIsActive = false;
        totalPointsAccumulated = 0;
        damageCount = 0;
        totalDamageCount = 0;
        bullets.removeAllElements();
        lifeImage = (Image) lifeBarImages.elementAt(0);

    }

    public int returnTotalPointsAccumulated() {
        return this.totalPointsAccumulated;
    }

    public void hasCollidedWithRamp(boolean vehicleIsAtRamp) {
        this.vehicleIsAtRamp = vehicleIsAtRamp;
    }

    private void makeVehicleRise(int carJumpingRampCount) {
        if (this.carJumpingRampCount <= carJumpingRampCount) {
            this.y -= 7;
            this.carJumpingRampCount++;
        } else {
            this.vehicleIsRising = false;
        }
    }

    private void makeVehicleFall() {
        if (this.carJumpingRampCount >= 0) {
            this.y += 7;
            this.carJumpingRampCount--;
        } else {
            vehicleIsAtRamp = false;
        }
    }

    public boolean getIsAtRamp() {
        return this.vehicleIsAtRamp;
    }
}

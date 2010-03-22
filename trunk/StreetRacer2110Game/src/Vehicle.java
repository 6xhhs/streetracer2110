
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Vehicle {

    private static final int MAX_BULLETS = 4;
    private static final int ROAD_TOP_Y_LIMIT = 260;

    private Image vehicleImage, damagedVehicleImage;
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
    private boolean drawDamagedVehicleIsActive;
    private boolean gameOverIsActive;
    private Vector lifeBarImages;

    public Vehicle(int screenWidth, int screenHeight, int carSelectedIndex) {

        this.carSelectedIndex = carSelectedIndex;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.x = 0;
        this.y = this.screenHeight - (screenHeight / 4);

        if (this.carSelectedIndex == 0) {
            try {
                vehicleImage = Image.createImage("/S Racer.png");
                damagedVehicleImage = Image.createImage("/S Racer Damaged.png");
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
                vehicleImage = Image.createImage("/M Racer.png");
                damagedVehicleImage = Image.createImage("/S Racer Damaged.png");
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
                vehicleImage = Image.createImage("/SM Racer.png");
                damagedVehicleImage = Image.createImage("/S Racer Damaged.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            CAR_WIDTH = 110;
            CAR_HEIGHT = 65;
            changeInX = 7;
            changeInY = 7;
            totalDamageReceived = 2;
        }

        lifeBarImages = new Vector();

        try {
            lifeBarImages.addElement(Image.createImage("/full life.png"));
            lifeBarImages.addElement(Image.createImage("/three fourths life.png"));
            lifeBarImages.addElement(Image.createImage("/half life.png"));
            lifeBarImages.addElement(Image.createImage("/one fourth life.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        lifeImage=(Image) lifeBarImages.elementAt(0);
        drawDamagedVehicleIsActive = false;
        gameOverIsActive = false;
        totalPointsAccumulated = 0;
        damageCount = 0;
        totalDamageCount = 0;
        bullets = new Vector();

        font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
    }

    public void dibujar(Graphics g) {

        if (drawDamagedVehicleIsActive) {
            g.drawImage(damagedVehicleImage, x, y, g.TOP | g.LEFT);
            drawDamagedVehicleIsActive = false;
        } else {
            g.drawImage(vehicleImage, x, y, g.TOP | g.LEFT);
        }
        if (!gameOverIsActive) {
            g.drawImage(lifeImage, 0, 0, g.TOP | g.LEFT);
        }
        g.setFont(font);
        g.setColor(0x000000);
        g.fillRect(160, 0, 110, 25);
        g.setColor(0xff6600);
        g.drawString("Score: " + totalPointsAccumulated, 162, 2, g.TOP | g.LEFT);
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

    public void agregarBullet() {
        if (bullets.size() < MAX_BULLETS) {
            bullets.addElement(new Pelota(this.bulletX, this.bulletY, 1));
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
            this.bulletY = this.y+2;
        }
    }

    public void hasCollided(boolean hasCollided, boolean addPoints) {
        if (hasCollided) {
            drawDamagedVehicleIsActive = true;

            if(addPoints){
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
                    System.out.println("Game Over!!");
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
        this.x = 0;
        this.y = this.screenHeight - (screenHeight / 4);
        drawDamagedVehicleIsActive = false;
        gameOverIsActive = false;
        totalPointsAccumulated = 0;
        damageCount = 0;
        totalDamageCount = 0;
        bullets.removeAllElements();
        lifeImage = (Image) lifeBarImages.elementAt(0);

    }
}






import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
/**
 * Encargada de los obstáculos que se encuentran dentro del juego
 * @author Manuel González Solano y Salvador Aguilar Galino
 */
public class Obstacles {

    private int obstacleWidth;
    private int obstacleHeight;
    private Image obstacleImage;
    private boolean obstacleHasCollided;
    private int obstacleX;
    private int obstacleY;
/**
 * Constructor, crea un obstáculo en la posición dada segun los valores de x,y dados y el índice
 * de selección de obstáculo.
 * @param obstacleX posición en x del obstáculo
 * @param obstacleY posición en y del obstáculo
 * @param obstacleSelect índice de selección de obstáculo
 */
    public Obstacles(int obstacleX, int obstacleY, int obstacleSelect) {
        this.obstacleX = obstacleX;
        this.obstacleY = obstacleY;

        this.obstacleHasCollided = false;

        if (obstacleSelect == 1) {
            try {
                obstacleImage = Image.createImage("/pothole.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            obstacleWidth = 50;
            obstacleHeight = 13;
        } else if (obstacleSelect == 2) {
            try {
                obstacleImage = Image.createImage("/cactus.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            obstacleWidth = 21;
            obstacleHeight = 23;
        } else if (obstacleSelect == 3) {
            try {
                obstacleImage = Image.createImage("/rock.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            obstacleWidth = 33;
            obstacleHeight = 22;
        }
    }

    /**
     * dibuja al obstáculo en la posición dada por sus coordenadas.
     * @param g graphics de la pantalla actual
     */
    public void dibujar(Graphics g) {
        g.drawImage(obstacleImage, obstacleX, obstacleY, g.TOP | g.LEFT);
    }

    /**
     * mueve al obstáculo, cambiando su posición en x.
     */
    public void actualizar() {
        this.obstacleX -= 15;
    }

    /**
     *
     * @return regresa la posición en x del obstáculo
     */
    public int getObstacleX() {
        return this.obstacleX;
    }
    /**
     *
     * @return regresa la posición en y del obstáculo
     */
    public int getObstacleY() {
        return this.obstacleY;
    }
    /**
     *
     * @return dice si el obstáculo tuvo una colisión o no.
     */
    public boolean obstacleHasCollided() {
        return this.obstacleHasCollided;
    }
    /**
     *
     * @return regresa el ancho del obstáculo
     */
    public int getObstacleWidth() {
        return this.obstacleWidth;
    }
    /**
     *
     * @return regresa la altura del obstáculo
     */
    public int getObstacleHeight() {
        return this.obstacleHeight;
    }

    /**
     * actualiza el estado del obstáculo con respecto a las colisiones
     * @param hasCollided determina si el obtsáculo tuvo una colisión o no
     */
    public void hasCollided(boolean hasCollided) {
        if (hasCollided) {
            this.obstacleHasCollided = hasCollided;
        }
    }

    /**
     * reinicia al obstáculo, tanto su posición como su estado de colisión.
     * @param newXValue nueva posición en x del obstáculo
     * @param newYValue nueva posición en y del obstáculo
     */
    public void resetObstacleCoordinates(int newXValue, int newYValue) {
        this.obstacleX = newXValue;
        this.obstacleY = newYValue;
        this.obstacleHasCollided = false;
    }
}

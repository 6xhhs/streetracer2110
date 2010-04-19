




import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Representa el manejo de los fondos de pantalla
 * @author Salvador Aguilar Galindo, Manuel González Solano
 * @version 1.0, Abril 2010
 *
 */
public class Background {

    private static final int LEVEL_X_LIMIT = -1100;
    private Image backgroundImage;
    private Image backgroundImage2;
    private int x;
    private int x2;
    private int alto;
    private int ancho;
    private boolean isSkyBackground;
    private int changeInX = 0;

    /**
     * Constructor crea una imagen desde una archivo, un booleano para saber si
     * es el fondo del cielo, y la velocidad del fondo.
     * @param archivo nombre del archivo que contiene la imagen.
     * @param isSkyBackground si el fondo se crea, esté representa el cielo.
     * @param speed velocidad con la que se mueve el fondo.
     * @throws IOException en caso de que las imagenes no existan.
     */
    public Background(String archivo, boolean isSkyBackground, int speed) throws IOException {

        this.isSkyBackground = isSkyBackground;

        backgroundImage = Image.createImage(archivo);
        changeInX = speed;

        this.ancho = 360;
        this.alto = 360;

        x = 0;

        if (!this.isSkyBackground) {
            backgroundImage2 = backgroundImage;
            x2 = this.ancho;
        }

    }

    /**
     *  Controla el movimiento de los fondos.
     */
    public void moveBackgroundImage() {

        if (isSkyBackground) {

            if (x <= LEVEL_X_LIMIT) {
                changeInX = 0;
            }
            x -= changeInX;

        } else {
            if (x <= -ancho) {
                x = ancho;
                x2 -= 0;
            } else {
                x -= changeInX;
                x2 -= changeInX;
            }

            if (x2 <= -ancho) {
                x2 = ancho;
                x -= 0;
            } else {
                x -= changeInX;
                x2 -= changeInX;
            }
        }
    }

    /**
     *
     * @param g Dibuja el fondo de pantalla correspondiente.
     */
    public void dibujar(Graphics g) {
        g.drawImage(backgroundImage, x, 0, g.TOP | g.LEFT);
        if (!isSkyBackground) {
            g.drawImage(backgroundImage2, x2, 0, g.TOP | g.LEFT);
        }
    }

    /**
     * Se encarga de  manejar la carretera, por donde se
     * desplazan los autos, obstaculos, y enemigos
     *
     */
    public void dibujarMovingStreet(Graphics g) {
        g.drawImage(backgroundImage, x, alto - backgroundImage.getHeight(), g.TOP | g.LEFT);
        g.drawImage(backgroundImage2, x2, alto - backgroundImage.getHeight(), g.TOP | g.LEFT);
    }

    /**
     *
     * @return la imagen del fondo
     */
    public Image returnImage() {
        return this.backgroundImage;
    }

    /**
     * Reinicia los valores de la x y los fondos de pantalla.
     */
    void resetValues() {
        x = 0;

        if (!this.isSkyBackground) {
            backgroundImage2 = backgroundImage;
            x2 = this.ancho;
        }
    }

    /**
     *
     * @return Regresa la posición del fondo.
     */
    public int returnXValue() {
        return this.x;
    }
}

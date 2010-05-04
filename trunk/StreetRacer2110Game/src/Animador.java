/**
 * Solicita un GameCanvas que ejecute los metodos actualizar y dibujar constantemente
 * @author Salvador Aguilar Galindo, Manuel González Solano
 * @version 1.0, Abril 2010
 */
public class Animador implements Runnable {

    private Juego juego;
    private boolean corriendo;  //when false, animador will cease to run
    private Thread thread;
    private final int FPS = 34;
    private final int RETARDO = 1000 / FPS;

    /**
     *Constructor, determina el juego actual.
     * @param juego se le asigna el valor de juego
     */
    public Animador(Juego juego) {
        this.juego = juego;  //crea un gui cuando se crea un objeto de animador
    }

    /**
     * Avisa si el juego está corriendo, indica al juego que se actualize y que se dibuje
     */
    public void run() {
        corriendo = true;
        while (corriendo) {

            //indicarle al gui que actualice a todos los objetos (posicion)
            juego.update();

            if (corriendo) {
                //Actualice la pantalla
                juego.draw();

                try {
                    Thread.sleep(RETARDO);
                } catch (InterruptedException ex) {}
            }
        }
        thread = null;
    }

    /**
     * Crea un thread nuevo, para que comienze a correr el juego
     */
    public void iniciar() {
        thread = new Thread(this);  //objeto independiente se crea con el metodo run()
        thread.start();     //llama al metodo run()
    }

    /**
     * Pone corriendo en false de tal manera que el
     * animador deje de correr
     */
    public void terminar() {
        corriendo = false;
    }
}

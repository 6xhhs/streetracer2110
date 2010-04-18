
//solicita un GameCanvas que ejecute los metodos update y draw constantemente
public class Animador implements Runnable {

    private Juego juego;    //gui deberia ser una interface
    private boolean corriendo;  //when false, animador will cease to run
    private Thread thread;
    private final int FPS = 34;
    private final int RETARDO = 1000 / FPS;

    public Animador(Juego juego) {
        this.juego = juego;  //crea un gui cuando se crea un objeto de animador

    }

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
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void iniciar() {

        thread = new Thread(this);  //objeto independiente se crea con el metodo run()
        thread.start();     //llama al metodo run()

    }

    public void terminar() {
        corriendo = false;
        thread.interrupt();
    }
}

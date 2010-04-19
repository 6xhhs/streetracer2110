




import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
/**
 * Encargada del manejo de música dentro de la aplicación.
 * @author Manuel González Solano y Salvador Aguilar Galindo
 */
public class MusicPlayer {

    private boolean isPlaying;
    private Player musicPlayer;
    private InputStream iS;
/**
 * Constructor, selecciona una canción segun el índice de selección.
 * @param index índice de selección de canción
 */
    public MusicPlayer(int index) {

        isPlaying = false;

        if (index == 0) {
            try {
                iS = getClass().getResourceAsStream("metallica.mid");
                musicPlayer = Manager.createPlayer(iS, "audio/midi");
            } catch (IOException ioe) {
                System.out.println("IO exception error");
            } catch (MediaException me) {
                System.out.println("Media exception error at constructor");
            }

        } else if (index == 1) {
            try {
                iS = getClass().getResourceAsStream("faint.mid");
                musicPlayer = Manager.createPlayer(iS, "audio/midi");
            } catch (IOException ioe) {
                System.out.println("IO exception error");
            } catch (MediaException me) {
                System.out.println("Media exception error at constructor");
            }
        } else if (index == 2) {
            try {
                iS = getClass().getResourceAsStream("Metallica I Disappear.mid");
                musicPlayer = Manager.createPlayer(iS, "audio/midi");
            } catch (IOException ioe) {
                System.out.println("IO exception error");
            } catch (MediaException me) {
                System.out.println("Media exception error at constructor");
            }
        } else if (index == 3) {
            try {
                iS = getClass().getResourceAsStream("faint.mid");
                musicPlayer = Manager.createPlayer(iS, "audio/midi");
            } catch (IOException ioe) {
                System.out.println("IO exception error");
            } catch (MediaException me) {
                System.out.println("Media exception error at constructor");
            }
        }
    }
/**
 * inicia al reproductor de música
 */
    public void startMusicPlayer() {
        try {
            musicPlayer.realize();
            musicPlayer.prefetch();
            musicPlayer.start();
        } catch (MediaException ex) {
            System.out.println("Media exception error at startMusicPlayer");
        }
        isPlaying = true;
    }

    /**
     * interrumpe al reproductor de música
     */
    public void stopMusicPlayer() {
        try {
            musicPlayer.stop();
        } catch (MediaException ex) {
            System.out.println("Media exception error at stopMusicPlayer");
        }
        isPlaying = false;
    }
/**
 * destruye al reproductor de música
 */
    void terminate() {
        musicPlayer.deallocate();
        musicPlayer = null;
    }

    /**
     *
     * @return el estado de reproducción de música
     */
    boolean isPlaying() {
        return isPlaying;
    }
}


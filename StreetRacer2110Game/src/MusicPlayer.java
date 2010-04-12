
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

public class MusicPlayer {

    private boolean isPlaying;
    private Player musicPlayer;
    private InputStream iS;

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

    public void stopMusicPlayer() {
        try {
            musicPlayer.stop();
        } catch (MediaException ex) {
            System.out.println("Media exception error at stopMusicPlayer");
        }
        isPlaying = false;
    }

    void terminate() {
        musicPlayer.deallocate();
        musicPlayer = null;
    }

    boolean isPlaying() {
        return isPlaying;
    }
}


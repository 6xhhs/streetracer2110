
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

public class MusicPlayer {

    private Player musicPlayer;
    private InputStream iS;

    public MusicPlayer(int index) {

        if (index == 0) {
            try {
                iS = getClass().getResourceAsStream("metallica.mid");
                musicPlayer = Manager.createPlayer(iS, "audio/midi");
            } catch (IOException ioe) {
                System.out.println("IO exception error");
            } catch (MediaException me) {
                System.out.println("Media exception error");
            }

        } else if (index == 1) {
            try {
                iS = getClass().getResourceAsStream("linkinParkFaint.mid");
                musicPlayer = Manager.createPlayer(iS, "audio/midi");
            } catch (IOException ioe) {
                System.out.println("IO exception error");
            } catch (MediaException me) {
                System.out.println("Media exception error");
            }
        } else if (index == 2) {
            try {
                iS = getClass().getResourceAsStream("macarenamix.mid");
                musicPlayer = Manager.createPlayer(iS, "audio/midi");
            } catch (IOException ioe) {
                System.out.println("IO exception error");
            } catch (MediaException me) {
                System.out.println("Media exception error");
            }
        } else if (index == 3) {
        }
    }

    public void startMusicPlayer() {
        try {
            musicPlayer.start();
        } catch (MediaException ex) {
            System.out.println("Media exception error");
        }
    }

    public void stopMusicPlayer() {
        try {
            musicPlayer.stop();
        } catch (MediaException ex) {
            System.out.println("Media exception error");
        }
    }
}


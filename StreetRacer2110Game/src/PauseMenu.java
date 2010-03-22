import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public class PauseMenu {

    private Graphics g;
    private int width;
    private int height;
    private int fontHeight;
    private Font font;

    public PauseMenu(GameCanvas canvas, Graphics g) {

        this.g = g;
        width = canvas.getWidth();
        height = canvas.getHeight();
    }

    public void drawPausedMenu(GameCanvas canvas, int i, boolean yesNoOptionsIsActive) {

        if (!yesNoOptionsIsActive) {
            g.setColor(0xff6600);
            if (i == 0) {
                g.fillRect(width / 3, ((height / 5) + fontHeight + 10), 130, fontHeight + 5);
            } else if (i == 1) {
                g.fillRect(width / 3, ((height / 5) + (2 * (fontHeight + 5))), 130, fontHeight + 5);
            } else if (i == 2) {
                g.fillRect(width / 3, ((height / 5) + (3 * (fontHeight + 5))), 130, fontHeight + 5);
            }
        }
        font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        fontHeight = font.getHeight();

        g.setFont(font);
        g.setColor(0x000000); // black

        g.drawString("Paused", width / 3, height / 5, g.TOP | g.LEFT);
        g.drawString("Resume", width / 3, ((height / 5) + fontHeight + 10), g.TOP | g.LEFT);
        g.drawString("Main Menu", width / 3, ((height / 5) + (2 * (fontHeight + 5))), g.TOP | g.LEFT);
        g.drawString("Exit", width / 3, ((height / 5) + (3 * (fontHeight + 5))), g.TOP | g.LEFT);
        if (yesNoOptionsIsActive) {
            font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
            g.setFont(font);
            g.setColor(0xff6600);
            g.fillRect(0, 320, width, fontHeight+10);
            g.setColor(0x000000);

            g.drawString("Are you sure?      C:Yes     D:No", 1, 320, g.TOP | g.LEFT);
        }

        canvas.flushGraphics();
    }
    
    public void drawAreYouSureMenu(GameCanvas canvas,Graphics g){
        g.drawString("Are you sure?      C:Yes     D:No", 1, 320, g.TOP | g.LEFT);
        canvas.flushGraphics();
    }


}

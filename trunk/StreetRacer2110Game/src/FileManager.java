
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class FileManager {

    private String file;
    private String data;
    private int totalPointsAccumulated;
    private static final int READ_FILE = 0;
    private static final int WRITE_FILE = 1;

    public FileManager(String file) {
        this.file = file;
        this.data = "";


    }

    public void readFile(Vector highScorePoints, Vector highScoreNames) {
        //read data
        try {
            FileConnection fc = (FileConnection) Connector.open("file:///e:/" + file, Connector.READ_WRITE);
            //FileConnection fc = (FileConnection) Connector.open("file:///datos.txt", Connector.READ_WRITE);
            if (!fc.exists()) {
                fc.create();
                OutputStream salida = fc.openOutputStream();
                OutputStreamWriter output = new OutputStreamWriter(salida);
                output.write("aaa 0\nbbb 0\nccc 0\nddd 0\neee 0");
                output.close();
            }
            InputStream entrada = fc.openInputStream();
            byte[] datos = new byte[512];
            int leidos = entrada.read(datos);   //returns -1 if nothing is read
            System.out.println(leidos);
            data = new String(datos, 0, leidos);
            ;
            System.out.println(data);
            entrada.close();
            fc.close();
            String tempData = data;
            while (tempData.indexOf("\n") != -1) {
                highScoreNames.addElement(tempData.substring(0, 3));
                highScorePoints.addElement(tempData.substring(4, tempData.indexOf("\n")));

                tempData = tempData.substring(tempData.indexOf("\n") + 1, tempData.length());
                System.out.println(tempData);
            }
            highScoreNames.addElement(tempData.substring(0, 3));
            highScorePoints.addElement(tempData.substring(4, tempData.length()));
            System.out.println(tempData);

        } catch (IOException ex) {
            System.out.println("something failed big time!");
        }
    }

    public void writeToFile(Vector highScorePoints, Vector highScoreNames) {

        //creating and writing to a document
        try {
            FileConnection fc = (FileConnection) Connector.open("file:///e:/" + file, Connector.READ_WRITE);
            if (!fc.exists()) {
                fc.create();
            }
            OutputStream salida = fc.openOutputStream();
            OutputStreamWriter output = new OutputStreamWriter(salida);
            for (int i = 0; i < highScorePoints.size(); i++) {
                if (i == highScorePoints.size() - 1) {
                    output.write(highScoreNames.elementAt(i) + " " + highScorePoints.elementAt(i));
                } else {
                    output.write(highScoreNames.elementAt(i) + " " + highScorePoints.elementAt(i) + "\n");

                }
            }
            output.close();
            salida.close();
            fc.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String returnReadData() {
        return this.data;
    }
}

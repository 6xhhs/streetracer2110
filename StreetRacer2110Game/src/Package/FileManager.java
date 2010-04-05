package Package;


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
        this.data="";


    }

    public void readFile(Vector highScorePoints, Vector highScoreNames) {
        //read data
        try {
            FileConnection fc = (FileConnection) Connector.open("file:///e:/"+file, Connector.READ_WRITE);
            //FileConnection fc = (FileConnection) Connector.open("file:///datos.txt", Connector.READ_WRITE);
            if (!fc.exists()) {
                throw new IOException("No existe el archivo!! aaah!!!!1 AAAh!!!");
            }
            InputStream entrada = fc.openInputStream();
            byte[] datos = new byte[512];
            int leidos = entrada.read(datos);   //returns -1 if nothing is read
            System.out.println(leidos);
            data = new String(datos, 0, leidos);;
            System.out.println(data);
            entrada.close();
            fc.close();
            String tempData = data;
            while(tempData.indexOf("\n")!= -1){
                System.out.println(tempData);
                highScoreNames.addElement(tempData.substring(0, 4));
                highScorePoints.addElement(tempData.substring(5,tempData.indexOf("\n")));
                
                tempData=tempData.substring(tempData.indexOf("\n")+1, tempData.length());
                System.out.println(tempData);
            }

             //highScorePoints.addElement(tempData.substring(0,tempData.indexOf(" ")));
             //highScoreNames.addElement(tempData.substring(tempData.indexOf(" ")+1, tempData.indexOf("\n")));
             System.out.println(tempData);
        } catch (IOException ex) {
            System.out.println("something failed big time!");
        }
    }

    public void writeToFile(Vector highScorePoints, Vector highScoreNames) {

        //creating and writing to a document
        try {
            FileConnection fc = (FileConnection) Connector.open("file:///e:/"+file, Connector.READ_WRITE);
            if (!fc.exists()) {
                fc.create();
            }
            OutputStream salida = fc.openOutputStream();
            OutputStreamWriter output = new OutputStreamWriter(salida);
            for(int i = 0;i<highScorePoints.size();i++){
                output.write(highScoreNames.elementAt(i)+" "+highScorePoints.elementAt(i)+"\n");
            }
            output.close();
            fc.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String returnReadData(){
        return this.data;
    }
}

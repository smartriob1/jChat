package Cliente;

import Servidor.Conexion;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Silvia
 */
public class Receptor extends Thread {

    private DataInputStream dis;
    private String respuesta;
    private Emisor emisor;

    public Receptor(DataInputStream dis, String respuesta, Emisor emisor) {
        this.dis = dis;
        this.respuesta = respuesta;
        this.emisor = emisor;
    }

    @Override
    public void run() {
        try {
            while (!Conexion.FIN_CLIENTE.equalsIgnoreCase(respuesta)) {
                respuesta = dis.readUTF();
                System.out.println(respuesta);
            }
            emisor.setFin(true);
        } catch (IOException ex) {
            Logger.getLogger(Receptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

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

    public Receptor(DataInputStream dis, String respuesta) {
        this.dis = dis;
        this.respuesta = respuesta;
    }

    @Override
    public void run() {
        try {
            while (!Conexion.FIN_CLIENTE.equalsIgnoreCase(respuesta)) {
                respuesta = dis.readUTF();
                System.out.println(respuesta);
            }
        } catch (IOException ex) {
            Logger.getLogger(Receptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

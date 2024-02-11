package cliente;

import conexion.Conexion;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hilo para recibir los mensajes del servidor.
 *
 * @author Silvia
 */
public class Receptor extends Thread {

    private DataInputStream dis;

    public Receptor(DataInputStream dis) {
        this.dis = dis;
    }

    @Override
    public void run() {
        try {
            String respuesta = "";
            while (!Conexion.FIN_CLIENTE.equalsIgnoreCase(respuesta)) {
                respuesta = dis.readUTF();
                System.out.println(respuesta);
            }
        } catch (IOException ex) {
            Logger.getLogger(Receptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

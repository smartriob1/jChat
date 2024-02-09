package Cliente;

import Servidor.Conexion;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cliente del chat.
 *
 * @author Silvia
 */
public class ClienteChat {

    public static void main(String[] args) {
        String direccion, nic;
        Socket servidor = null;
        try {
            //Comprobamos los parametros en args
            if (args.length == 2) {
                direccion = args[0];
                nic = args[1];
                servidor = new Socket(direccion, Conexion.PUERTO());
                DataInputStream dis = new DataInputStream(servidor.getInputStream());
                DataOutputStream dos = new DataOutputStream(servidor.getOutputStream());
                dos.writeUTF(nic);
                Emisor emisor = new Emisor(dos);
                Receptor receptor = new Receptor(dis);
                receptor.start();
                emisor.start();
                receptor.join();
                emisor.join();
                servidor.close();
            } else {
                System.out.println("El comando lleva los siguientes parámetros java ClienteChat <direccion> <nic>");
            }

        } catch (IOException ex) {
            Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Conexión cerrada");
    }

}

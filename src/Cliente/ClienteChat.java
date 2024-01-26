package Cliente;

import Servidor.Conexion;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Silvia
 */
public class ClienteChat {

    public static String respuesta = null;

    //los parametros son en args[]
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String direccion, nic, mensaje;
        try {
//            do {
//                direccion = args[0];
//                nic = args[1];
//            } while (!direccion.equalsIgnoreCase(Conexion.SERVIDOR() + ":" + Conexion.PUERTO()));
            nic = sc.nextLine();
            Socket servidor = new Socket(Conexion.SERVIDOR(), Conexion.PUERTO());
            DataInputStream dis = new DataInputStream(servidor.getInputStream());
            DataOutputStream dos = new DataOutputStream(servidor.getOutputStream());
            Receptor receptor = new Receptor(dis, respuesta);
            Emisor emisor = new Emisor(dos, respuesta);
            //enviar el nic
            dos.writeUTF(nic);
            respuesta = dis.readUTF();
            System.out.println(respuesta);
            receptor.start();
            emisor.start();
            receptor.join();
            emisor.join();
            servidor.close();

        } catch (IOException ex) {
            //Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Conexión cerrada");
    }

}

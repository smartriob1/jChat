package Cliente;

import Servidor.Conexion;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

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
            do {
                direccion = args[0];
                nic = args[1];
            } while (!direccion.equalsIgnoreCase(Conexion.SERVIDOR() + ":" + Conexion.PUERTO()));

            Socket servidor = new Socket(Conexion.SERVIDOR(), Conexion.PUERTO());
            DataInputStream dis = new DataInputStream(servidor.getInputStream());
            DataOutputStream dos = new DataOutputStream(servidor.getOutputStream());
            Receptor receptor = new Receptor(dis);
            Emisor emisor = new Emisor(dos);
            //enviar el nic
            dos.writeUTF(nic);
            respuesta = dis.readUTF();
            System.out.println(respuesta);

            while (!Conexion.FIN_CLIENTE.equalsIgnoreCase(respuesta)) {
//              receptor.start();
//              mensaje = sc.nextLine();
//              //Enviamos el comando
//              dos.writeUTF(mensaje);
//              //El servidor responde
//              respuesta = dis.readUTF();
//              System.out.println(respuesta);
            }

            servidor.close();

        } catch (IOException ex) {
            //Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Conexión cerrada");
    }

}

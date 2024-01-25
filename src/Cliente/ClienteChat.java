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

    private final static String CONEXION_CLIENTE = "java ClienteChat";
    public static String respuesta = null;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String conexion, comando, direccion, nic, mensaje;
        try {
            do {
                System.out.print("> ");
                conexion = sc.nextLine();
                String[] campos = conexion.split(" ");
                comando = campos[0] + " " + campos[1];
                direccion = campos[2];
                nic = campos[3];
            } while (!comando.equalsIgnoreCase(CONEXION_CLIENTE) && !direccion.equalsIgnoreCase(Conexion.SERVIDOR() + ":" + Conexion.PUERTO()));

            Socket servidor = new Socket(Conexion.SERVIDOR(), Conexion.PUERTO());
            DataInputStream dis = new DataInputStream(servidor.getInputStream());
            DataOutputStream dos = new DataOutputStream(servidor.getOutputStream());

            //enviar el nic
            dos.writeUTF(nic);
            respuesta = dis.readUTF();
            System.out.println(respuesta);

            while (!Conexion.FIN_CLIENTE.equalsIgnoreCase(respuesta)) {
                mensaje = sc.nextLine();
                //Enviamos el comando
                dos.writeUTF(mensaje);
                //El servidor responde
                respuesta = dis.readUTF();
                System.out.println(respuesta);
            }

            servidor.close();

        } catch (IOException ex) {
            //Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Conexión cerrada");
    }

}
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

    public final static String IP = "127.0.0.1";
    private final static String CONEXION_CLIENTE = "java ClienteChat";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String conexion, comando, direccion, nic, respuesta, mensaje;
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
            DataInputStream in = new DataInputStream(servidor.getInputStream());
            DataOutputStream out = new DataOutputStream(servidor.getOutputStream());
            //Enviamos el nombre del cliente
            out.writeUTF(nic);
            //El servidor responde
            respuesta = in.readUTF();
            System.out.println("Bienvenido/a. Introduzca un nombre para su usuario");
            do {
                System.out.print("Nombre: ");
                mensaje = sc.nextLine();
                //Enviamos el nombre del cliente
                out.writeUTF(mensaje);
                //El servidor responde
                respuesta = in.readUTF();
                System.out.println(respuesta);
            } while (!Conexion.FIN_CONN_CLIENTE.equalsIgnoreCase(respuesta));

            servidor.close();

        } catch (IOException ex) {
            //Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Conexión cerrada");
    }
}

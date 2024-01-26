package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Silvia
 */
public class ServidorChat {

    private static ServerSocket server;
    public static List<HiloClienteServidor> CONEXIONES_CLIENTES;
    public static HashMap<String, Integer> HISTORIAL_CLIENTES;

    public static void main(String[] args) {
        try {
            CONEXIONES_CLIENTES = new ArrayList();
            HISTORIAL_CLIENTES = new HashMap();
            server = new ServerSocket(Conexion.PUERTO());
            System.out.println("Servidor escuchando en " + server.getLocalSocketAddress());

            while (true) {
                Socket cliente = server.accept();
                HiloClienteServidor hiloSocket = new HiloClienteServidor(cliente);
                hiloSocket.start();
            }

        } catch (IOException ex) {
            //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Apagando...");
    }
}
